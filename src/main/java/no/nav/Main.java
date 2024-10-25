package no.nav;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.resultat.Resultat;
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

      Resultat resultat = dagpengerKalkulator.kalkulerDagsats();

      Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann",
          SaksbehandlerSpesialisering.INNVILGET);

      saksbehandler.behandleResultat(resultat);

      skrivUtEnkeltResultat(saksbehandler, resultat);
    } catch (Exception e) {
      System.out.println("Kunne ikke kalkulere dagsats: " + e.getMessage());
    }
  }

  private static void skrivUtEnkeltResultat(Saksbehandler saksbehandler, Resultat resultat) {
    System.out.println("====================================");
    System.out.println("NAV Dagpengerkalkulator");
    System.out.println("Saksbehandler: " + saksbehandler.hentNavn() + " - " + saksbehandler.hentSpesialisering());
    System.out.println("Resultat av dagpengeforespørsel: " + resultat.hentStatus() + " - " + resultat.hentDagsats() + " kr per dag.");
    System.out.println("====================================");
  }
}