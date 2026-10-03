package fr.fruityhedgeh0g.utilities.web;

import io.quarkus.vertx.http.runtime.filters.Filters;
import io.vertx.core.http.HttpHeaders;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * The static handler marks everything it serves "public, immutable, max-age=86400". Right for the hashed
 * /assets/* bundles, wrong for the pages: a browser would keep an index.html pointing at the bundles of a
 * previous deployment, or the index.html served in place of a bundle not deployed yet, and show a blank page.
 * Pages are revalidated on every load instead.
 */
@ApplicationScoped
public class HtmlCacheFilter {

    void register(@Observes Filters filters) {
        filters.register(rc -> {
            rc.addHeadersEndHandler(v -> {
                String type = rc.response().headers().get(HttpHeaders.CONTENT_TYPE);
                if (type != null && type.startsWith("text/html")) {
                    rc.response().putHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
                }
            });
            rc.next();
        }, 100);
    }
}
