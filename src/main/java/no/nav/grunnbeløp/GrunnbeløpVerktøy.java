package no.nav.grunnbeløp;

import java.io.IOException;


/**
 * Verktøy med forskjellige hjelpemetoder for å kalkulere grunnbeløpsverdier.
 *
 * <p>
 * Denne klassen benytter NAVs grunnbeløp API for å hente dagens grunnbeløp, og tilbyr metoder for å
 * kalkulere beløp relatert til dagpenger basert på grunnbeløpet.
 *
 * @author Emil Elton Nilsen, Sigurd Riseth
 * @version 1.1
 * @see GrunnbeløpAPI
 */
public class GrunnbeløpVerktøy {

  private static final double MINIMUM_MULTIPLIKATOR_DAGPENGER = 1.5; // 1.5G
  private static final double MAKS_MULTIPLIKATOR_DAGPENGER = 6.0; // 6G
  private double grunnbeløp;

  /**
   * Konstruktør for {@link GrunnbeløpVerktøy}-klassen.
   *
   * <p>
   * Denne konstruktøren initialiserer et nytt {@link GrunnbeløpVerktøy}-objekt ved å hente dagens
   * grunnbeløp fra NAVs grunnbeløp API. Dersom det oppstår problemer med tilkoblingen til API'et,
   * vil en feilmelding skrives ut til konsollen.
   */
  public GrunnbeløpVerktøy() {
    try {
      this.grunnbeløp = new GrunnbeløpAPI().hentGrunnbeløp();
    } catch (IOException | InterruptedException exception) {
      System.out.println("Problemer med tilkobling til grunnbeløp API'et" + exception.getMessage());
    }
  }

  /**
   * Kalkulerer det totale grunnbeløpet for et gitt antall år.
   *
   * @param antallÅr antall år med grunnbeløp som skal kalkuleres.
   * @return det totale grunnbeløpet for det angitte antallet år.
   */
  public double hentTotaltGrunnbeløpForGittAntallÅr(int antallÅr) {
    return this.grunnbeløp * antallÅr;
  }

  /**
   * Kalkulerer minimum årslønn en person må tjene det siste året for å ha rett på dagpenger.
   *
   * @return minimum årslønn for rett på dagpenger, beregnet som 1.5G basert på dagens grunnbeløp.
   */
  public double hentMinimumÅrslønnForRettPåDagpenger() {
    return this.grunnbeløp * MINIMUM_MULTIPLIKATOR_DAGPENGER;
  }

  /**
   * Kalkulerer det maksimale årlige dagpengegrunnlaget en person kan ha.
   *
   * @return det maksimale årlige dagpengegrunnlaget, beregnet som 6G basert på dagens grunnbeløp.
   */
  public double hentMaksÅrligDagpengegrunnlag() {
    return this.grunnbeløp * MAKS_MULTIPLIKATOR_DAGPENGER;
  }
}
