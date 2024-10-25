package no.nav.saksbehandler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import no.nav.resultat.Resultat;
import no.nav.resultat.ResultatStatus;
import org.junit.jupiter.api.Test;

class SaksbehandlerTest {

  @Test
  void TestFeilSpesialisering() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
        SaksbehandlerSpesialisering.INNVILGET);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);

    assertThrows(IllegalArgumentException.class, () -> saksbehandler.behandleResultat(resultat));
    assertEquals(ResultatStatus.UBEHANDLET, resultat.hentStatus(),
        "Forventet UBEHANDLET for feil spesialisering");
  }

  @Test
  void TestInnvilget() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
        SaksbehandlerSpesialisering.INNVILGET);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.INNVILGET);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.INNVILGET, resultat.hentStatus(),
        "Forventet INNVILGET for spesialisering INNVILGET");
  }

  @Test
  void TestAvslag() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
        SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.AVSLÅTT, resultat.hentStatus(),
        "Forventet AVSLÅTT for spesialisering AVSLAG_FOR_LAV_INNTEKT");
  }

  @Test
  void TestInnvilgetMedMaksSats() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
        SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS);
    Resultat resultat = new Resultat(500.0, SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS);

    saksbehandler.behandleResultat(resultat);
    assertEquals(ResultatStatus.INNVILGET, resultat.hentStatus(),
        "Forventet INNVILGET for spesialisering INNVILGET_MED_MAKSSATS");
  }

  @Test
  void testBehandleUbehandledeResultater() {
    Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
        SaksbehandlerSpesialisering.INNVILGET);

    List<Resultat> resultater = List.of(
        new Resultat(500.0, SaksbehandlerSpesialisering.INNVILGET),
        new Resultat(1000.0, SaksbehandlerSpesialisering.INNVILGET),
        new Resultat(0, SaksbehandlerSpesialisering.AVSLAG_FOR_LAV_INNTEKT),
        new Resultat(2000.0, SaksbehandlerSpesialisering.INNVILGET_MED_MAKSSATS)
    );

    saksbehandler.behandleUbehandledeResultater(resultater.iterator());

    assertAll("Verifiser status på behandlede resultater",
        () -> assertEquals(ResultatStatus.INNVILGET, resultater.get(0).hentStatus(),
            "Forventet INNVILGET for resultat 1"),
        () -> assertEquals(ResultatStatus.INNVILGET, resultater.get(1).hentStatus(),
            "Forventet INNVILGET for resultat 2"),
        () -> assertEquals(ResultatStatus.UBEHANDLET, resultater.get(2).hentStatus(),
            "Forventet UBEHANDLET for resultat 3"),
        () -> assertEquals(ResultatStatus.UBEHANDLET, resultater.get(3).hentStatus(),
            "Forventet UBEHANDLET for resultat 4")
    );
  }

}