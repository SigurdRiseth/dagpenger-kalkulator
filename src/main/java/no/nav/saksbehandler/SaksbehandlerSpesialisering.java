package no.nav.saksbehandler;

/**
 * Representerer en saksbehandlers spesialisering.
 *
 * <p>
 * Mulige spesialiseringer er:
 * <ul>
 * <li><code>AVSLAG_FOR_LAV_INNTEKT</code>: Spesialisering for å avslå søknader med lav inntekt.</li>
 * <li><code>INNVILGET</code>: Spesialisering for å innvilge søknader.</li>
 * <li><code>INNVILGET_MED_MAKSSATS</code>: Spesialisering for å innvilge søknader med maksimal sats.</li>
 * </ul>
 * </p>
 *
 * @author Sigurd Riseth
 * @version 1.0
 * @see no.nav.saksbehandler.Saksbehandler
 */
public enum SaksbehandlerSpesialisering {
  AVSLAG_FOR_LAV_INNTEKT,
  INNVILGET,
  INNVILGET_MED_MAKSSATS
}