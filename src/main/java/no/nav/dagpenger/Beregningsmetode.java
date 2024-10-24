package no.nav.dagpenger;

/**
 * Representerer hvilken metode som skal brukes for å beregne dagpenger.
 *
 * <p>
 * Mulige metoder er:
 * <ul>
 * <li><code>SISTE_ÅRSLØNN</code>: Bruker siste års lønn for å beregne dagpenger.</li>
 * <li><code>GJENNOMSNITTET_AV_TRE_ÅR</code>: Bruker gjennomsnittet av de tre siste års lønn for å beregne dagpenger.</li>
 * <li><code>MAKS_ÅRLIG_DAGPENGERGRUNNLAG</code>: Bruker maksimalt årlig dagpengergrunnlag for å beregne dagpenger.</li>
 * </ul>
 * </p>
 *
 * @author Sigurd Riseth
 * @version 1.0
 * @see no.nav.dagpenger.DagpengerKalkulator
 */
public enum Beregningsmetode {
  SISTE_ÅRSLØNN,
  GJENNOMSNITTET_AV_TRE_ÅR,
  MAKS_ÅRLIG_DAGPENGERGRUNNLAG
}