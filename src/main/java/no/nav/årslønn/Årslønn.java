package no.nav.årslønn;

/**
 * Representerer en persons lønn for et kalenderår.
 *
 * <p>
 * Holder på informasjon om hvilket år lønnen tilhører og selve lønnen for det kalenderåret.
 * </p>
 *
 * @author Emil Elton Nilsen, Sigurd Riseth
 * @version 1.1
 */
public class Årslønn {

  private int åretForLønn;
  private double årslønn;

  /**
   * Konstruktør for å initialisere en ny instans av Årslønn med gitt år og lønn.
   *
   * @param åretForLønn året som lønnen tilhører. Må være 2010 eller nyere.
   * @param årslønn     lønnen for det gitte året. Kan ikke være negativ.
   * @throws IllegalArgumentException hvis årslønnen er negativ eller året er under 2010.
   */
  public Årslønn(int åretForLønn, double årslønn) throws IllegalArgumentException {
    if (åretForLønn < 2010) {
      throw new IllegalArgumentException("Årstallet for lønning kan ikke være under 2010.");
    }
    if (årslønn < 0) {
      throw new IllegalArgumentException("Årslønnen kan ikke være negativ.");
    }
    this.åretForLønn = åretForLønn;
    this.årslønn = årslønn;
  }

  /**
   * Henter året som lønnen tilhører.
   *
   * @return året for lønnen.
   */
  public int hentÅretForLønn() {
    return åretForLønn;
  }

  /**
   * Henter årslønnen.
   *
   * @return årslønnen.
   */
  public double hentÅrslønn() {
    return årslønn;
  }

}
