package no.nav.dagpenger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.årslønn.Årslønn;

/**
 * Kalkulator for å beregne dagpenger for personer i Norge basert på dagens grunnbeløp (1G).
 * <p>
 * For at en person skal ha rett på dagpenger, må ett av følgende krav være oppfylt:
 * <ol>
 *     <li>Gjennomsnittsinntekten de siste tre årene må være høyere enn 3G.</li>
 *     <li>Inntekten det siste året må være høyere enn 1,5G.</li>
 * </ol>
 *
 * Dersom en person har rett på dagpenger, vurderes følgende for å kalkulere dagsatsen:
 * <ol>
 *     <li>Sammenligning av gjennomsnittlig årslønn de tre siste årene med årslønnen det siste året.</li>
 *     <li>Hvis årslønnen det siste året er høyere enn gjennomsnittlig årslønn, vurderes om den er høyere enn 6G.</li>
 * </ol>
 *
 * Dagsatsen beregnes med utgangspunkt i 260 arbeidsdager per år, i stedet for 365 dager.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */

public class DagpengerKalkulator {

  public final GrunnbeløpVerktøy grunnbeløpVerktøy;

  public final List<Årslønn> årslønner;

  public DagpengerKalkulator() {
    this.grunnbeløpVerktøy = new GrunnbeløpVerktøy();
    this.årslønner = new ArrayList<>();
  }

  /**
   * Hvis en person har rett på dagpenger, vil den kalkulere dagsatsen en person har rett på. Hvis
   * ikke en person har rett på dagpenger, vil metoden returnere 0kr som dagsats, som en antagelse
   * på at det er det samme som å ikke ha rett på dagpenger.
   *
   * @return dagsatsen en person har rett på.
   */
  public double kalkulerDagsats() {
    double dagsats = 0;

    int arbeidsdagerIÅret = 260;
    if (harRettigheterTilDagpenger() == true) {
      if (velgBeregningsMetode() == "SISTE_ÅRSLØNN") {
        dagsats = Math.ceil(hentÅrslønnVedIndeks(0).hentÅrslønn() / arbeidsdagerIÅret);
      } else if (velgBeregningsMetode() == "GJENNOMSNITTET_AV_TRE_ÅR") {
        dagsats = Math.ceil((summerNyligeÅrslønner(3) / 3) / arbeidsdagerIÅret);
      } else if (velgBeregningsMetode() == "MAKS_ÅRLIG_DAGPENGERGRUNNLAG") {
        dagsats = Math.ceil(grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag() / arbeidsdagerIÅret);
      }
    }

    return dagsats;
  }

  /**
   * Sjekker om en person har rettighet til dagpenger eller ikke.
   *
   * @return om personen har rett på dagpenger.
   */
  public boolean harRettigheterTilDagpenger() {
    boolean harRettigheter = false;

    if (summerNyligeÅrslønner(3) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(3)) {
      harRettigheter = true;
    } else if (hentÅrslønnVedIndeks(0).hentÅrslønn()
        >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger()) {
      harRettigheter = true;
    }

    return harRettigheter;
  }

  /**
   * Velger hva som skal være beregnings metode for dagsats ut ifra en person sine årslønner.
   *
   * @return beregnings metode for dagsats.
   */
  public String velgBeregningsMetode() {
    String beregningsMetode;

    if (hentÅrslønnVedIndeks(0).hentÅrslønn() > (summerNyligeÅrslønner(3) / 3)) {
      beregningsMetode = "SISTE_ÅRSLØNN";
      if (hentÅrslønnVedIndeks(0).hentÅrslønn()
          > grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag()) {
        beregningsMetode = "MAKS_ÅRLIG_DAGPENGERGRUNNLAG";
      }
    } else {
      beregningsMetode = "GJENNOMSNITTET_AV_TRE_ÅR";
    }

    return beregningsMetode;
  }

  public void leggTilÅrslønn(Årslønn årslønn) {
    this.årslønner.add(årslønn);
    this.sorterÅrslønnerBasertPåNyesteÅrslønn();
  }

  /**
   * Henter årslønnen i registeret basert på dens posisjon i registeret ved gitt indeks.
   *
   * @param indeks posisjonen til årslønnen.
   * @return årslønnen ved gitt indeks.
   */
  public Årslønn hentÅrslønnVedIndeks(int indeks) {
    return this.årslønner.get(indeks);
  }

  /**
   * Summemer sammen antall årslønner basert på gitt parameter.
   *
   * @param antallÅrÅSummere antall år med årslønner vi vil summere.
   * @return summen av årslønner.
   */
  public double summerNyligeÅrslønner(int antallÅrÅSummere) {
    double sumAvNyligeÅrslønner = 0;

    if (antallÅrÅSummere <= this.årslønner.size()) {
      List<Årslønn> subÅrslønnListe = new ArrayList<>(this.årslønner.subList(0, antallÅrÅSummere));

      for (Årslønn årslønn : subÅrslønnListe) {
        sumAvNyligeÅrslønner += årslønn.hentÅrslønn();
      }
    }

    return sumAvNyligeÅrslønner;
  }

  /**
   * Sorterer registeret slik at den nyligste årslønnen er det først elementet i registeret. Først
   * blir årslønnene i registeret sortert ut at den eldstre årslønnen skal først i registeret,
   * deretter blir registeret reversert.
   */
  public void sorterÅrslønnerBasertPåNyesteÅrslønn() {
    this.årslønner.sort(Comparator.comparingInt(Årslønn::hentÅretForLønn));
    Collections.reverse(this.årslønner);
  }
}
