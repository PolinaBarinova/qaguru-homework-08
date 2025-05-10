package tests.vkvideo;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class VkVideoTests {

    @BeforeEach
    void prepareEnvironment() {
        Configuration.baseUrl = "https://vkvideo.ru";
    }

    @ValueSource(strings = {
            "/video-217986831_456239673", "/video-217986831_456239673?t=1h10m4s"
    })
    @ParameterizedTest(name = "Воспроизведение видео, открытого по ссылке вида {0} неавторизованным пользователем")
    @DisplayName("Воспроизведение видео, открытого по ссылке неавторизованным пользователем")
    @Tags({
            @Tag("WEB"),
            @Tag("ANONYM")
    })
    void playVideoOpenedViaLinkByAnonymTest(String relativeUrl) {
        open(relativeUrl);
        $(".videoplayer_ui").shouldBe(visible);
        $(".videoplayer_ui").shouldHave(attribute("data-state", "playing"));
    }

    @CsvFileSource(resources = "/test_data/searchResultsMatchTheSearchQueryTest.csv")
    @ParameterizedTest(name = "Проверка наличия в результатах поиска видео {1} при выполнении поискового запроса {0}")
    @DisplayName("Проверка наличия в результах поиска видео соответсвующего поисковому запроса")
    @Tag("WEB")
    void searchResultsMatchTheSearchQueryTest(String searchQuery, String expectedText) {
        $("[data-testid=top-search-video-input]").setValue(searchQuery).pressEnter();
        $(".VideoLayout__search").shouldHave(text(expectedText));
        $(".vkuiSearch__controls").click();

    }

    static Stream<Arguments> availabilityOfListCategoriesInDifferentSectionsVideoTest() {
        return Stream.of(
                Arguments.of("Детям", "Маленьким героям о больших подвигах", "Садись рядом, Мишка!"),
                Arguments.of("Фильмы", "Мамы: от любви до хаоса", "День матери"),
                Arguments.of("Шоу", "Одно безумное преступление", "Одно безумное преступление | Выпуск №4: Величайшие самозванцы в истории")
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Воспроизведение видео, открытого из плейлиста раздела {0} неавторизованным пользователем")
    @DisplayName("Проверка воспроизведения видео, открытого из плейлиста неавторизованным пользователем")
    @Tags({
            @Tag("WEB"),
            @Tag("ANONYM")
    })
    void availabilityOfListCategoriesInDifferentSectionsVideoTest(String sectionsVideo, String titlePlaylist, String titleVideo) {
        open("https://vkvideo.ru");
        $$(".MenuList__item").find(text(sectionsVideo)).click();
        $$(".VideoCard__title").find(text(titlePlaylist)).click();
        $$("[data-testid=video_card_title]").find(text(titleVideo)).click();
        $("[data-testid=video_modal_title]").shouldHave(text(titleVideo));
        $(".videoplayer_ui").shouldHave(attribute("data-state", "playing"));
    }
}

