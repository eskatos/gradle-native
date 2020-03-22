package dev.nokee.docs

import dev.nokee.docs.fixtures.html.BakedHtmlFixture
import dev.nokee.docs.fixtures.html.HtmlTag
import spock.lang.Specification

class ProperBakedHtmlTest extends Specification {
	def "has alt text on all images"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().collect { it.findAll(HtmlTag.IMG) }.each {
			it*.assertHasAltText()
		}
	}

	def "has proper canonical links"() {
		expect:
		def fixture = new BakedHtmlFixture(new File(System.getProperty('bakedContentDirectory')).toPath())
		fixture.findAllHtml().each {
			def canonicalLinks = it.findAll(HtmlTag.LINK).findAll { it.isCanonical() }
			assert canonicalLinks.size() == 1
			assert canonicalLinks.first().href.present
			assert canonicalLinks.first().href.get() == it.getCanonicalPath()
		}
	}
}
