package sk.mkiss.interview.pss.testutils

import io.restassured.config.MatcherConfig
import io.restassured.config.RestAssuredConfig
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode


class JsonStringMatcher private constructor(private val expectedJson: String) : TypeSafeDiagnosingMatcher<String>() {

    override fun describeTo(description: Description) {
        description.appendText(expectedJson)
    }

    override fun matchesSafely(item: String, mismatchDescription: Description): Boolean {
        val compareResult = JSONCompare.compareJSON(expectedJson, item, JSONCompareMode.STRICT_ORDER)

        if (compareResult.failed()) {
            mismatchDescription.appendValue(item)
            mismatchDescription.appendText(
                    """
                        |
                        | JSON Comparison failed:
                        | 
                    """.trimMargin()
            )
            mismatchDescription.appendText(compareResult.message)
        }

        return compareResult.passed()
    }

    companion object {
        fun matchesJson(expectedJson: String) = JsonStringMatcher(expectedJson)
    }
}

val HAMCREST_MATCHER_CONFIG: RestAssuredConfig = RestAssuredConfig.newConfig()
        .matcherConfig(MatcherConfig.matcherConfig()
                .errorDescriptionType(MatcherConfig.ErrorDescriptionType.HAMCREST))