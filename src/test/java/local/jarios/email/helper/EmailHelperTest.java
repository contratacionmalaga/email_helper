package local.jarios.email.helper;

import static org.assertj.core.api.Assertions.assertThat;

import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.Test;

class EmailHelperTest {

  @Test
  void shouldEscapeDynamicTableValues() {
    String row = EmailHelper.getFila("<script>alert('key')</script>", "<b>value & more</b>");

    assertThat(row)
        .contains("&lt;script&gt;alert(&#39;key&#39;)&lt;/script&gt;")
        .contains("&lt;b&gt;value &amp; more&lt;/b&gt;")
        .doesNotContain("<script>")
        .doesNotContain("<b>value");
  }

  @Test
  void shouldEscapeDynamicHeaderTitle() {
    String header = EmailHelper.getCabeceraBody("<h1>Injected</h1>");

    assertThat(header)
        .contains("&lt;h1&gt;Injected&lt;/h1&gt;")
        .doesNotContain("<h1>Injected</h1>");
  }

  @Test
  void shouldEscapeErrorEmailBodyValues() {
    RuntimeException ex = new RuntimeException("<failure & detail>");

    EmailData email =
        ErrorEmailBuilder.build(ex, "<context>", "from@example.com", "to@example.com");

    assertThat(email.body())
        .contains("&lt;context&gt;")
        .contains("&lt;failure &amp; detail&gt;")
        .doesNotContain("<context>")
        .doesNotContain("<failure & detail>");
  }

  @Test
  void shouldAllowCustomErrorSubjectAndLimitStacktrace() {
    RuntimeException ex = new RuntimeException("failure");

    EmailData email =
        ErrorEmailBuilder.build(
            ex, "context", "from@example.com", "to@example.com", "Custom subject", 12);

    assertThat(email.subject()).isEqualTo("Custom subject");
    assertThat(email.body())
        .contains("[stacktrace truncado]")
        .doesNotContain("local.jarios.email.helper.EmailHelperTest");
  }
}
