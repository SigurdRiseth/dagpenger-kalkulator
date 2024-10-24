package no.nav.saksbehandler;

/**
 * Representerer en saksbehandler.
 *
 * <p>
 * En saksbehandler har et navn og en spesialisering som bestemmer hvilke resultater den kan behandle.
 * Saksbehandleren kan godkjenne eller avslå resultater basert på sin spesialisering og resultatets status.
 * </p>
 *
 * @author Sigurd Riseth
 * @version 1.0
 * @see no.nav.saksbehandler.SaksbehandlerSpesialisering
 * @see no.nav.saksbehandler.Resultat
 */
public class Saksbehandler {

  private String navn;
  private SaksbehandlerSpesialisering spesialisering;

  /**
   * Oppretter en ny saksbehandler med gitt navn og spesialisering.
   *
   * @param navn navnet til saksbehandleren.
   * @param spesialisering spesialiseringen som saksbehandleren har.
   */
  public Saksbehandler(String navn, SaksbehandlerSpesialisering spesialisering) {
    this.navn = navn;
    this.spesialisering = spesialisering;
  }

  public String hentNavn() {
    return navn;
  }

  public SaksbehandlerSpesialisering hentSpesialisering() {
    return spesialisering;
  }

  /**
   * Behandler et resultat ved å godkjenne eller avslå det basert på spesialiseringen til saksbehandleren
   * og resultatets spesialisering.
   *
   * <p>
   * Hvis resultatets spesialisering er {@link SaksbehandlerSpesialisering#AVSLAG_FOR_LAV_INNTEKT},
   * settes resultatets status til {@link ResultatStatus#AVSLÅTT}.
   * For andre spesialiseringer settes resultatets status til {@link ResultatStatus#INNVILGET}.
   * </p>
   *
   * @param resultat resultatet som skal behandles.
   */
  public void behandleResultat(Resultat resultat) {
    if (resultat.hentSpesialisering() == SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT) {
      resultat.setStatus(ResultatStatus.AVSLÅTT);
    } else {
      resultat.setStatus(ResultatStatus.INNVILGET);
    }
  }

}
