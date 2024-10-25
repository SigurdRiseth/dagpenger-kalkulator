package no.nav.saksbehandler;

import java.util.Iterator;
import no.nav.resultat.Resultat;
import no.nav.resultat.ResultatStatus;

/**
 * Representerer en saksbehandler.
 *
 * <p>
 * En saksbehandler har et navn og en spesialisering som bestemmer hvilke resultater den kan
 * behandle. Saksbehandleren kan godkjenne eller avslå resultater basert på sin spesialisering og
 * resultatets status.
 * </p>
 *
 * @author Sigurd Riseth
 * @version 1.0
 * @see no.nav.saksbehandler.SaksbehandlerSpesialisering
 * @see Resultat
 */
public class Saksbehandler {

  private String navn;
  private SaksbehandlerSpesialisering spesialisering;

  /**
   * Oppretter en ny saksbehandler med gitt navn og spesialisering.
   *
   * @param navn           navnet til saksbehandleren.
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
   * Behandler et resultat ved å godkjenne eller avslå det basert på spesialiseringen til
   * saksbehandleren og resultatets spesialisering.
   *
   * <p>
   * Saksbehandleren kan kun behandle resultater som samsvarer med sin egen spesialisering.
   * Hvis resultatets spesialisering ikke samsvarer med saksbehandlerens spesialisering,
   * vil det ikke behandles.
   * </p>
   *
   * <p>
   * Hvis resultatets spesialisering er {@link SaksbehandlerSpesialisering#AVSLAG_FOR_LAV_INNTEKT},
   * settes resultatets status til {@link ResultatStatus#AVSLÅTT}. For andre spesialiseringer settes
   * resultatets status til {@link ResultatStatus#INNVILGET}.
   * </p>
   *
   * @param resultat resultatet som skal behandles.
   * @throws IllegalArgumentException hvis resultatet har feil spesialisering for saksbehandleren.
   */
  public void behandleResultat(Resultat resultat) {
    if (resultat.hentSpesialisering() != spesialisering) {
      return;
    }

    switch (spesialisering) {
      case AVSLAG_FOR_LAV_INNTEKT:
        resultat.setStatus(ResultatStatus.AVSLÅTT);
        break;
      default:
        resultat.setStatus(ResultatStatus.INNVILGET);
        break;
    }
  }

  /**
   * Behandler ubehandlede resultater som samsvarer med saksbehandlerens spesialisering.
   *
   * <p>
   * Itererer over en liste med ubehandlede resultater og behandler de som samsvarer med
   * saksbehandlerens spesialisering.
   * </p>
   *
   * @param iterator iterator over ubehandlede resultater.
   */
  public void behandleUbehandledeResultater(Iterator<Resultat> iterator) {
    while (iterator.hasNext()) {
      Resultat resultat = iterator.next();
      if (resultat.hentSpesialisering() == spesialisering) {
        behandleResultat(resultat);
      }
    }
  }


}
