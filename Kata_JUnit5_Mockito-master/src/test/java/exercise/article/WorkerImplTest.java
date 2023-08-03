package exercise.article;

import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class WorkerImplTest {
    private WorkerImpl worker;
    private Library libraryMock;

    @BeforeEach
    void setUp() {
        libraryMock = mock(Library.class);
        doNothing().when(libraryMock).updateCatalog();
        worker = new WorkerImpl(libraryMock);
    }

    @Test
    void testPrepareArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Title 1", "Content 1", "Author 1", LocalDate.of(2023, 1, 1)));
        articles.add(new Article(null, "Content 2", "Author 2", null));
        articles.add(new Article("Title 3", null, "Author 3", LocalDate.of(2023, 3, 3)));
        articles.add(new Article("Title 4", "Content 4", "", LocalDate.of(2023, 4, 4)));
        articles.add(new Article("Title 5", "Content 5", "Author 5", null));

        List<Article> result = worker.prepareArticles(articles);

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Content 1", result.get(0).getContent());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals(LocalDate.of(2023, 1, 1), result.get(0).getCreationDate());

        assertNotNull(result.get(1).getTitle());
        assertEquals("Content 5", result.get(1).getContent());
        assertEquals("Author 5", result.get(1).getAuthor());
        assertNotNull(result.get(1).getCreationDate());
    }

    @Test
    void testAddNewArticles_WithoutDuplicates() {
        List<Article> articles = Arrays.asList(
                new Article("Title 1", "Content", "Author", LocalDate.of(2023, 1, 1)),
                new Article("Title 2", "Content", "Author", LocalDate.of(2022, 1, 1)));

        worker.addNewArticles(articles);

        verify(libraryMock, times(1)).store(2022, articles.subList(1, 2));
        verify(libraryMock, times(1)).store(2023, articles.subList(0, 1));
        verify(libraryMock, times(1)).updateCatalog();
    }

    @Test
    void testAddNewArticles_WithDuplicates() {
        List<Article> articles = Arrays.asList(
                new Article("Title 1", "Content", "Author", LocalDate.of(2023, 1, 1)),
                new Article("Title 1", "Content", "Author", LocalDate.of(2022, 1, 1))
        );

        worker.addNewArticles(articles);

        verify(libraryMock, times(1)).store(2022, articles.subList(1, 2));
        verify(libraryMock, times(1)).updateCatalog();
    }
    @Test
    void testGetCatalog() {
        List<String> titles = new ArrayList<>();
        titles.add("Title 1");
        titles.add("Title 3");
        titles.add("Title 2");

        when(libraryMock.getAllTitles()).thenReturn(titles);

        String catalog = worker.getCatalog();

        String expectedCatalog = "Список доступных статей:\n" +
                "    Title 1\n" +
                "    Title 2\n" +
                "    Title 3\n";
        assertEquals(expectedCatalog, catalog);
    }
    @Test
    void testAddNewArticles_SortingAndCatalogUpdate() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Title 1", "Content", "Author",  LocalDate.of(2023, 1, 1)));
        articles.add(new Article("Title 2", "Content", "Author", LocalDate.of(2022,2,3)));

        worker.addNewArticles(articles);

        verify(libraryMock, times(1)).store(eq(2022), anyList());
        verify(libraryMock, times(1)).store(eq(2023), anyList());

        verify(libraryMock, times(1)).updateCatalog();
    }
    @Test
    void testAddNewArticles_EmptyList() {
        List<Article> articles = new ArrayList<>();

        worker.addNewArticles(articles);

        verifyNoInteractions(libraryMock);
    }
}