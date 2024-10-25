package no.nav.saksbehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.resultat.Resultat;
import no.nav.resultat.ResultatStatus;
import org.junit.jupiter.api.Test;

class SaksbehandlerTest {

  @Test
  void TestFeilSpesialisering() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", SaksbehandlerSpesialisering.INNVILGET);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);

    assertThrows(IllegalArgumentException.class, () -> saksbehandler.behandleResultat(resultat));
    assertEquals(ResultatStatus.UBEHANDLET, resultat.hentStatus());
  }

  @Test
  void TestInnvilget() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", SaksbehandlerSpesialisering.INNVILGET);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.INNVILGET);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.INNVILGET, resultat.hentStatus());
  }

  @Test
  void TestAvslag() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.AVSLÅTT, resultat.hentStatus());
  }

  @Test
  void TestInnvilgetMedMaksSats() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.INNVILGET, resultat.hentStatus());
  }

}