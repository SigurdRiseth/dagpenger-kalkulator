package no.nav.dagpenger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import no.nav.grunnbeløp.Beregningsmetode;
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
 * <p>
 * Dersom en person har rett på dagpenger, vurderes følgende for å kalkulere dagsatsen:
 * <ol>
 *     <li>Sammenligning av gjennomsnittlig årslønn de tre siste årene med årslønnen det siste året.</li>
 *     <li>Hvis årslønnen det siste året er høyere enn gjennomsnittlig årslønn, vurderes om den er høyere enn 6G.</li>
 * </ol>
 * <p>
 * Dagsatsen beregnes med utgangspunkt i 260 arbeidsdager per år, i stedet for 365 dager.
 * </p>
 *
 * @author Emil Elton Nilsen, Sigurd Riseth
 * @version 1.0
 */

public class DagpengerKalkulator {

  private static final int ARBEIDSDAGER_I_ÅRET = 260;

  public final GrunnbeløpVerktøy grunnbeløpVerktøy;
  public final List<Årslønn> årslønner;

  public DagpengerKalkulator() {
    this.grunnbeløpVerktøy = new GrunnbeløpVerktøy();
    this.årslønner = new ArrayList<>();
  }

  /**
   * Kalkulerer dagsatsen en person har rett på, dersom personen oppfyller kravene for dagpenger.
   *
   * <p>
   * Dagsatsen beregnes basert på enten siste årslønn, gjennomsnittet av de tre siste årene, eller
   * maksimalt årlig dagpengegrunnlag, avhengig av beregningsmetoden.
   * </p>
   * <p>
   * Hvis personen ikke har rett på dagpenger, returnerer metoden 0 kr.
   * <p>
   * Beregningsmetodene som brukes er:
   * <ul>
   *   <li><code>SISTE_ÅRSLØNN</code>: Dagsatsen beregnes basert på den siste årslønnen.</li>
   *   <li><code>GJENNOMSNITTET_AV_TRE_ÅR</code>: Dagsatsen beregnes basert på gjennomsnittet av de tre siste årslønnene.</li>
   *   <li><code>MAKS_ÅRLIG_DAGPENGERGRUNNLAG</code>: Dagsatsen begrenses av det maksimale årlige dagpengegrunnlaget.</li>
   * </ul>
   * </p>
   *
   * @return dagsatsen personen har rett på, i norske kroner. Hvis personen ikke har rett på
   * dagpenger, returneres 0 kr.
   * @throws IllegalArgumentException hvis beregningsmetoden er ugyldig eller uventet.
   */

  public double kalkulerDagsats() throws IllegalArgumentException {
    double dagsats = 0;

    if (harRettigheterTilDagpenger()) {

      dagsats = switch (velgBeregningsMetode()) {
        case SISTE_ÅRSLØNN ->
            Math.ceil(hentÅrslønnVedIndeks(0).hentÅrslønn() / ARBEIDSDAGER_I_ÅRET);
        case GJENNOMSNITTET_AV_TRE_ÅR ->
            Math.ceil((summerNyligeÅrslønner(3) / 3) / ARBEIDSDAGER_I_ÅRET);
        case MAKS_ÅRLIG_DAGPENGERGRUNNLAG -> Math.ceil(
            grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag() / ARBEIDSDAGER_I_ÅRET);
      };
    }

    return dagsats;
  }

  /**
   * Sjekker om en person har rettighet til dagpenger eller ikke.
   *
   * <p>
   * En person har rett på dagpenger hvis en av følgende krav er oppfylt:
   * <ol>
   *   <li>Gjennomsnittlig lønn de siste 3 årene er høyere eller lik 3G.</li>
   *   <li>Siste årslønn er høyere eller lik 1.5G.</li>
   * </ol>
   * </p>
   *
   * @return true hvis personen har rett på dagpenger, false ellers.
   */
  public boolean harRettigheterTilDagpenger() {
    // Sjekker om gjennomsnittlig lønn de siste 3 årene er høyere eller lik 3G
    if (summerNyligeÅrslønner(3) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(3)) {
      return true;
    }

    // Sjekker om siste årslønn er høyere eller lik 1.5G
    if (hentÅrslønnVedIndeks(0).hentÅrslønn()
        >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger()) {
      return true;
    }

    return false;
  }


  /**
   * Velger hva som skal være beregnings metode for dagsats ut ifra en person sine årslønner.
   *
   * @return beregnings metode for dagsats.
   */
  public Beregningsmetode velgBeregningsMetode() {
    Beregningsmetode beregningsMetode;

    if (hentÅrslønnVedIndeks(0).hentÅrslønn() > (summerNyligeÅrslønner(3) / 3)) {
      beregningsMetode = Beregningsmetode.SISTE_ÅRSLØNN;
      if (hentÅrslønnVedIndeks(0).hentÅrslønn()
          > grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag()) {
        beregningsMetode = Beregningsmetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
      }
    } else {
      beregningsMetode = Beregningsmetode.GJENNOMSNITTET_AV_TRE_ÅR;
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
   * Summerer sammen de siste n antall årslønner basert på gitt parameter. Hvis antall år å summere
   * er større enn tilgjengelige årslønner, vil alle tilgjengelige årslønner bli summert.
   *
   * @param antallÅrÅSummere antall år med årslønner som skal summeres. Må være større enn 0.
   * @return summen av årslønner for de spesifiserte årene.
   * @throws IllegalArgumentException hvis antallÅrÅSummere er negativt eller <code>null</code>.
   */
  public double summerNyligeÅrslønner(int antallÅrÅSummere) throws IllegalArgumentException {
    if (antallÅrÅSummere <= 0) {
      throw new IllegalArgumentException("Antall år å summere må være større enn 0.");
    }

    int årÅSummere = Math.min(antallÅrÅSummere, this.årslønner.size());

    return this.årslønner.stream()
        .limit(årÅSummere)
        .mapToDouble(Årslønn::hentÅrslønn)
        .sum();
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
