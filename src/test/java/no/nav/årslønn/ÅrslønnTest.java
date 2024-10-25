package no.nav.årslønn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ÅrslønnTest {

  /**
   * Tester at en gyldig konstruktør skaper en riktig Årslønn-instans.
   */
  @Test
  void testGyldigÅrslønnOgÅr() {
    Årslønn årslønn = new Årslønn(2022, 500000.0);

    assertEquals(2022, årslønn.hentÅretForLønn(), "Årslønnens år er feil.");
    assertEquals(500000.0, årslønn.hentÅrslønn(), "Årslønnens beløp er feil.");
  }

  /**
   * Tester at konstruktøren kaster IllegalArgumentException for et år under 2010.
   */
  @Test
  void testUgyldigÅrUnder2010() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Årslønn(2009, 500000.0);
    });

    assertEquals("Årstallet for lønning kan ikke være under 2010.", exception.getMessage(),
        "Feilmelding er feil.");
  }

  /**
   * Tester at konstruktøren kaster IllegalArgumentException for negativ årslønn.
   */
  @Test
  void testNegativÅrslønn() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Årslønn(2022, -50000.0);
    });

    assertEquals("Årslønnen kan ikke være negativ.", exception.getMessage(),
        "Feilmelding er feil.");
  }

  /**
   * Tester at årslønn kan være 0 uten feil.
   */
  @Test
  void testNullÅrslønn() {
    Årslønn årslønn = new Årslønn(2022, 0.0);

    assertEquals(2022, årslønn.hentÅretForLønn(), "Årslønnens år er feil.");
    assertEquals(0.0, årslønn.hentÅrslønn(), "Årslønnens beløp er feil.");
  }

}
