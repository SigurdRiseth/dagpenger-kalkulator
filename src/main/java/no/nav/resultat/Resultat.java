package no.nav.resultat;

import no.nav.saksbehandler.SaksbehandlerSpesialisering;

/**
 * Representerer et resultat av en dagpengeforespørsel.
 *
 * <p>
 * Resultatet inneholder informasjon om dagsatsen som er beregnet, samt hvilken spesialisering som
 * har behandlet forespørselen.
 * </p>
 *
 * @author Sigurd Riseth
 * @version 1.0
 * @see no.nav.saksbehandler.Saksbehandler
 * @see no.nav.saksbehandler.SaksbehandlerSpesialisering
 * @see ResultatStatus
 */
public class Resultat {

  private final SaksbehandlerSpesialisering spesialisering;
  private final double dagsats;
  private ResultatStatus status;

  /**
   * Oppretter et nytt resultat.
   *
   * <p> Resultatet er initialisert som ubehandlet. </p>
   *
   * @param dagsats        dagsatsen som er beregnet.
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

  public ResultatStatus hentStatus() {
    return status;
  }

  public double hentDagsats() {
    return dagsats;
  }
}
