package br.com.nicolas.integrationTests.swagger;

import br.com.nicolas.config.TestConfigs;
import br.com.nicolas.integrationTests.testContainers.AbstractIntegrationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationsTest {

	@Test
	@DisplayName("Test Should Display Swagger Ui Page")
	void testShouldDisplaySwaggerUiPage() {

		var content = given()
				.basePath("/swagger-ui/index.html")
				.port(TestConfigs.SERVER_PORT)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		assertTrue(content.contains("Swagger UI"));

	}

}
