package exercise.worker;

import exercise.article.Article;
import exercise.article.Library;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkerImpl implements Worker {
    private final Library library;

    public WorkerImpl(Library library) {
        this.library = library;
    }

    @Override
    public void addNewArticles(List<Article> articles) {
        Map<Integer, List<Article>> newArticles = prepareArticles(articles).stream()
                .collect(Collectors.groupingBy(a -> a.getCreationDate().getYear()));
        newArticles.forEach((year, articlesToAdd) -> {
            List<String> existingTitles = library.getAllTitles();
            List<Article> noDuplicateArticles = articlesToAdd.stream()
                    .filter(article -> !existingTitles.contains(article.getTitle()))
                    .collect(Collectors.toList());
            library.store(year, noDuplicateArticles);
        });
        updateIfNecessary(newArticles);
    }

    @Override
    public String getCatalog() {
        StringBuilder sb = new StringBuilder("Список доступных статей:\n");
        library.getAllTitles()
                .stream()
                .sorted(String::compareTo)
                .forEachOrdered(title -> sb.append("    ").append(title).append("\n"));
        return sb.toString();
    }

    @Override
    public List<Article> prepareArticles(List<Article> articles) {
        List<Article> result = articles
                .stream()
                .filter(this::isArticleCorrect)
                .toList();
        result.forEach(this::prepareDate);
        return result;
    }

    private boolean isArticleCorrect(Article article) {
        return !(nullOrBlank(article.getTitle()) ||
                nullOrBlank(article.getContent()) ||
                nullOrBlank(article.getAuthor()));
    }

    private boolean nullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    private void prepareDate(Article article) {
        if (article.getCreationDate() == null) {
            article.setCreationDate(LocalDate.now());
        }
    }

    private void updateIfNecessary(Map<Integer, List<Article>> newArticles) {
        if (!newArticles.isEmpty()) {
            for (Map.Entry<Integer, List<Article>> entry : newArticles.entrySet()) {
                int year = entry.getKey();
                List<Article> articlesToAdd = entry.getValue();
                if (shouldUpdateCatalog(year, articlesToAdd)) {
                    library.updateCatalog();
                    break; // Since we updated for one year, no need to continue
                }
            }
        }
    }

        private boolean shouldUpdateCatalog(int year, List<Article> articlesToAdd) {
            List<String> existingTitles = library.getAllTitles();

            for (Article article : articlesToAdd) {
                if (!existingTitles.contains(article.getTitle())) {
                    return true;
                }
            }

            return false;
        }
}
