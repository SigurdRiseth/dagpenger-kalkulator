package no.nav.saksbehandler;

import java.util.List;

public class Saksbehandler {

  private String navn;
  private SaksbehandlerSpesialisering spesialisering;

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
   * Godkjenner eller avslår et resultat.
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
