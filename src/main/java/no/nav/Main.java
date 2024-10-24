package no.nav;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.saksbehandler.Resultat;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.saksbehandler.SaksbehandlerSpesialisering;
import no.nav.årslønn.Årslønn;

public class Main {

  public static void main(String[] args) {
    try {
      DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator();
      dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 500000));
      dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2022, 450000));
      dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2021, 400000));

      Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", SaksbehandlerSpesialisering.INNVILGET);

      Resultat resultat = dagpengerKalkulator.kalkulerDagsats();

      saksbehandler.behandleResultat(resultat);

      System.out.println("Saksbehandler: " + saksbehandler.hentNavn());
      System.out.println("Resultat av dagpengeforespørsel: " + resultat.hentSpesialisering() + " - " + resultat.hentDagsats() + " kr.");
    }
    catch (Exception e) {
      System.out.println("Kunne ikke kalkulere dagsats: " + e.getMessage());
    }
  }
}