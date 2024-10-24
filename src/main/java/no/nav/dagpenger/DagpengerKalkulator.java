package no.nav.dagpenger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.saksbehandler.Resultat;
import no.nav.saksbehandler.SaksbehandlerSpesialisering;
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
 * @version 1.1
 * @see Beregningsmetode
 * @see GrunnbeløpVerktøy
 * @see Årslønn
 */

public class DagpengerKalkulator {

  private static final int ARBEIDSDAGER_I_ÅRET = 260;

  private final GrunnbeløpVerktøy grunnbeløpVerktøy;
  private final Map<Integer, Årslønn> årslønner;

  public DagpengerKalkulator() {
    this.grunnbeløpVerktøy = new GrunnbeløpVerktøy();
    this.årslønner = new HashMap<>();
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
  public Resultat kalkulerDagsats() throws IllegalArgumentException {
    double dagsats = 0;
    SaksbehandlerSpesialisering spesialisering = SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT;

    if (harRettigheterTilDagpenger()) {
      switch (velgBeregningsMetode()) {
        case SISTE_ÅRSLØNN:
          dagsats = beregnDagsats(summerNyligeÅrslønner(1));
          spesialisering = SaksbehandlerSpesialisering.INNVILGET;
          break;
        case GJENNOMSNITTET_AV_TRE_ÅR:
          dagsats = beregnDagsats(summerNyligeÅrslønner(3) / 3);
          spesialisering = SaksbehandlerSpesialisering.INNVILGET;
          break;
        case MAKS_ÅRLIG_DAGPENGERGRUNNLAG:
          dagsats = beregnDagsats(grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag());
          spesialisering = SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS;
          break;
      }
    }

    return new Resultat(dagsats, spesialisering);
  }

  /**
   * Beregner dagsatsen basert på en gitt årslønn.
   *
   * @param årslønn årslønnen som dagsatsen skal beregnes ut ifra.
   * @return dagsatsen basert på årslønnen.
   */
  private double beregnDagsats(double årslønn) {
    return Math.ceil(årslønn / ARBEIDSDAGER_I_ÅRET);
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
    // Sjekker case 1 (3år >= 3G)
    if (summerNyligeÅrslønner(3) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(3)) {
      return true;
    }

    // Sjekker case 2 (sist år >= 1.5G)
    if (summerNyligeÅrslønner(1)
        >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger()) {
      return true;
    }

    return false;
  }

  /**
   * Velger hvilken beregningsmetode som skal brukes for å kalkulere dagsats, basert på personens
   * årslønner.
   *
   * @return beregningsmetoden som skal brukes for dagsatsberegningen.
   * @throws IllegalStateException hvis det ikke finnes årslønn tilgjengelig.
   */
  public Beregningsmetode velgBeregningsMetode() throws IllegalStateException {
    if (årslønner.isEmpty()) {
      throw new IllegalStateException("Ingen årslønn tilgjengelig for beregning.");
    }

    double sisteÅrslønn = summerNyligeÅrslønner(1);
    double gjennomsnittTreÅr = summerNyligeÅrslønner(3) / 3;
    double maksDagpengegrunnlag = grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag();

    // Velg beregningsmetode basert på inntektsgrunnlag
    if (sisteÅrslønn > gjennomsnittTreÅr) {
      if (sisteÅrslønn >= maksDagpengegrunnlag) {
        return Beregningsmetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
      }
      return Beregningsmetode.SISTE_ÅRSLØNN;
    } else {
      return Beregningsmetode.GJENNOMSNITTET_AV_TRE_ÅR;
    }
  }

  /**
   * Legger til en ny årslønn i registeret.
   *
   * @param årslønn årslønnen som skal legges til i registeret.
   */
  public void leggTilÅrslønn(Årslønn årslønn) {
    this.årslønner.put(årslønn.hentÅretForLønn(), årslønn);
  }

  /**
   * Summerer sammen de siste n antall årslønner basert på gitt parameter. Hvis antall år å summere
   * er større enn tilgjengelige årslønner, vil alle tilgjengelige årslønner bli summert.
   *
   * @param antallÅrÅSummere antall år med årslønner som skal summeres. Må være større enn 0.
   * @return summen av årslønner for de spesifiserte årene.
   * @throws IllegalArgumentException hvis antallÅrÅSummere er negativt eller null (0).
   */
  public double summerNyligeÅrslønner(int antallÅrÅSummere) throws IllegalArgumentException {
    if (antallÅrÅSummere <= 0) {
      throw new IllegalArgumentException("Antall år å summere må være større enn 0.");
    }
    return årslønner.values().stream()
        .sorted(Comparator.comparing(Årslønn::hentÅretForLønn).reversed())
        .limit(antallÅrÅSummere)
        .mapToDouble(Årslønn::hentÅrslønn)
        .sum();
  }

}
