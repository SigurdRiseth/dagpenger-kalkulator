package no.nav.saksbehandler;

public class Resultat {
  private final SaksbehandlerSpesialisering spesialisering;
  private ResultatStatus status;
  private final double dagsats;

  public Resultat(double dagsats, SaksbehandlerSpesialisering spesialisering) {
    this.dagsats = dagsats;
    this.spesialisering = spesialisering;
    this.status = ResultatStatus.UBEHANDLET;
  }

  public SaksbehandlerSpesialisering hentSpesialisering() {
    return spesialisering;
  }

  public boolean erUbehandlet() {
    return status == ResultatStatus.UBEHANDLET;
  }

  public void setStatus(ResultatStatus status) {
    this.status = status;
  }

  public double hentDagsats() {
    return dagsats;
  }
}
