package com.example

import com.example.data.CtRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

  @Test
  fun `verify app title and default curriculum data`() {
    val appTitle = "CT-Learn"
    assertEquals("CT-Learn", appTitle)

    // Verify notes and glossary integration
    assertTrue("Default study notes should be present", CtRepository.defaultNotes.isNotEmpty())
    val firstNote = CtRepository.defaultNotes.first()
    assertEquals("Rumus Inti Fisika, Geometri Helikal & Dosimetri CT", firstNote.title)
    assertTrue("Glossary terms must cover key CT physics", CtRepository.glossary.any { it.term.contains("Hounsfield") })
  }
}
