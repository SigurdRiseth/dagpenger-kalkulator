package no.nav.saksbehandler;

/**
 * Representerer statusen til et resultat.
 *
 * <p>
 * Mulige statuskoder er:
 * <ul>
 * <li><code>UBEHANDLET</code>: Resultatet er ikke behandlet.</li>
 * <li><code>INNVILGET</code>: Resultatet er innvilget.</li>
 * <li><code>AVSLÅTT</code>: Resultatet er avslått.</li>
 * </ul>
 * </p>
 *
 * @see no.nav.saksbehandler.Resultat
 * @author Sigurd Riseth
 * @version 1.0
 */
public enum ResultatStatus {
  UBEHANDLET,
  INNVILGET,
  AVSLÅTT
}
