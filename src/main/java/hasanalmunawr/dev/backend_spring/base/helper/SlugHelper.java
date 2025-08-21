package hasanalmunawr.dev.backend_spring.base.helper;

import org.springframework.stereotype.Component;
import java.text.Normalizer;
import java.util.Locale;

@Component
public class SlugHelper {

    public static String toSlug(String input) {
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD);
        slug = slug.replaceAll("[^\\w\\s-]", "").trim().replaceAll("[\\s]+", "-").toLowerCase(Locale.ENGLISH);
        return slug;
    }

}
