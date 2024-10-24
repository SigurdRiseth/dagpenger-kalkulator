package no.nav.saksbehandler;

/**
 * Representerer et resultat av en dagpengeforespørsel.
 *
 * <p>
 * Resultatet inneholder informasjon om dagsatsen som er beregnet, samt hvilken spesialisering som
 * har behandlet forespørselen.
 * </p>
 *
 * @see no.nav.saksbehandler.Saksbehandler
 * @see no.nav.saksbehandler.SaksbehandlerSpesialisering
 * @see no.nav.saksbehandler.ResultatStatus
 * @author Sigurd Riseth
 * @version 1.0
 */
public class Resultat {
  private final SaksbehandlerSpesialisering spesialisering;
  private ResultatStatus status;
  private final double dagsats;

  /**
   * Oppretter et nytt resultat.
   *
   * <p> Resultatet er initialisert som ubehandlet. </p>
   *
   * @param dagsats dagsatsen som er beregnet.
   * @param spesialisering spesialiseringen som har behandlet forespørselen.
   */
  public Resultat(double dagsats, SaksbehandlerSpesialisering spesialisering) {
    this.dagsats = dagsats;
    this.spesialisering = spesialisering;
    this.status = ResultatStatus.UBEHANDLET;
  }

  public SaksbehandlerSpesialisering hentSpesialisering() {
    return spesialisering;
  }

  public void setStatus(ResultatStatus status) {
    this.status = status;
  }

  public double hentDagsats() {
    return dagsats;
  }
}
