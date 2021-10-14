package answers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(DataProviderRunner.class)
public class RestAssuredExamplesTest {

    @Test
    public void getUserData_verifyName_shouldBeLeanneGraham() {

        given().
        when().
            get("http://jsonplaceholder.typicode.com/users/1").  // Do a GET call to the specified resource
        then().
            assertThat().                                           // Assert that the value of the element 'name'
            body("name", equalTo("Leanne Graham"));       // in the response body equals 'Leanne Graham'
    }

    @Test
    public void logAllRequestData() {

        given().
            log().all().
        when().
            get("http://jsonplaceholder.typicode.com/users/1").
        then().
            assertThat().
            body("name", equalTo("Leanne Graham"));
    }

    @Test
    public void logAllResponseData() {

        given().
        when().
            get("http://jsonplaceholder.typicode.com/users/1").
        then().
            log().all().
        and().
            assertThat().
            body("name", equalTo("Leanne Graham"));
    }


    @Test
    public void getUserData_verifyStatusCodeAndContentType() {

        given().
        when().
            get("http://jsonplaceholder.typicode.com/users/1").
        then().
            assertThat().
            statusCode(200).
        and().
            contentType(ContentType.JSON);
    }

    @Test
    public void useQueryParameter() {

        given().
            log().all().
            queryParam("text", "testcase"). // add query param
        when().
            get("http://md5.jsontest.com").
        then().
            assertThat().
            body("md5", equalTo("7489a25fc99976f06fecb807991c61cf"));
    }

    @Test
    public void usePathParameter() {

        given().
            log().all().
            pathParam("userId",1).
        when().
            get("http://jsonplaceholder.typicode.com/users/{userId}").
        then().
            assertThat().
            body("name", equalTo("Leanne Graham"));
    }

    @Test
    @DataProvider({
            "1, Leanne Graham",
            "2, Ervin Howell",
            "3, Clementine Bauch"
    })
    public void checkNameForUser
        (int userId, String expectedUserName) {

        given().
            pathParam("userId", userId).
        when().
            get("http://jsonplaceholder.typicode.com/users/{userId}").
        then().
            assertThat().
            body("name",equalTo(expectedUserName));
    }




    // ===================== Mock by ResponseSpecification ==============================
    private static ResponseSpecification responseSpec;  // Specify How The Expected Response Must Look Like

    @BeforeClass
    public static void createResponseSpec() {

        responseSpec =
            new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    @Test
    public void useResponseSpec() {
        given().
        when().
            get("http://jsonplaceholder.typicode.com/users/1").
        then().
            spec(responseSpec).
        and().
            body("name", equalTo("Leanne Graham"));
    }


    private static RequestSpecification requestSpec; // Specify How The Expected Request Must Look Like

    @BeforeClass
    public static void createRequestSpec() {

        requestSpec =
            new RequestSpecBuilder().
                setBaseUri("http://jsonplaceholder.typicode.com/").
                build();
    }

    @Test
    public void useRequestSpec() {

        given().
            log().all().
            spec(requestSpec).
        when().
            get("/users/1").
        then().
            assertThat().
            statusCode(200);
    }


}