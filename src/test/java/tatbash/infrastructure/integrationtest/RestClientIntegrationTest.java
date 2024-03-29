package tatbash.infrastructure.integrationtest;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import tatbash.infrastructure.utils.JunitTags;

/**
 * <p>
 * {@link RestClientTest} annotation is used for testing and mocking {@link RestTemplate} requests.
 * </p>
 * <p>
 * See <a href="https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/">an article</a>.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Tag(JunitTags.INTEGRATION_TEST_PROFILE)
@ActiveProfiles({"integration-test"})
@RestClientTest
@TestPropertySource
public @interface RestClientIntegrationTest {

  /**
   * @see RestClientTest#value()
   */
  @AliasFor(annotation = RestClientTest.class, attribute = "value")
  Class<?>[] value() default {};

  /**
   * @see TestPropertySource#properties()
   */
  @AliasFor(annotation = TestPropertySource.class, attribute = "properties")
  String[] properties() default {};
}
