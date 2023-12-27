package org.bupt.hse.retrieval.utils;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.bupt.hse.retrieval.entity.Product;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-27
 */
public class Elasticsearch {
    private static ElasticsearchClient client = null;
    private static ElasticsearchAsyncClient asyncClient = null;

    private static synchronized void makeConnection() {
        // Create the low-level client
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "password"));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost("10.112.67.227", 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestClient restClient = builder.build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
    }

    public static void main(String[] args) throws IOException {
        makeConnection();

        // Index data to an index products
        Product product = new Product("abc", "Bag", 42);

        IndexRequest<Object> indexRequest = new IndexRequest.Builder<>()
                .index("products")
                .id("abc")
                .document(product)
                .build();

        client.index(indexRequest);

        Product product1 = new Product("efg", "Bag", 42);

        client.index(builder -> builder
                .index("products")
                .id(product1.getId())
                .document(product1)
        );

        // Search for a data
        TermQuery query = QueryBuilders.term()
                .field("name")
                .value("bag")
                .build();

        SearchRequest request = new SearchRequest.Builder()
                .index("products")
                .query(query._toQuery())
                .build();

        SearchResponse<Product> search =
                client.search(
                        request,
                        Product.class
                );
        System.out.println("11111111111111111111");
        for (Hit<Product> hit: search.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
        }

        SearchResponse<Product> search1 = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("bag"))
                                )),
                Product.class);

        System.out.println("2222222222222222222222");
        for (Hit<Product> hit: search1.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
        }

        // Splitting complex DSL
        TermQuery termQuery = TermQuery.of(t ->t.field("name").value("bag"));

        SearchResponse<Product> search2 = client.search(s -> s
                        .index("products")
                        .query(termQuery._toQuery()),
                Product.class
        );
        System.out.println("3333333333333333333");
        for (Hit<Product> hit: search2.hits().hits()) {
            Product pd = hit.source();
            System.out.println(pd);
        }

        // Creating aggregations
        SearchResponse<Void> search3 = client.search(b-> b
                        .index("products")
                        .size(0)
                        .aggregations("price-histo", a -> a
                                .histogram(h -> h
                                        .field("price")
                                        .interval(20.0)
                                )
                        ),
                Void.class
        );

        long firstBucketCount = search3.aggregations()
                .get("price-histo")
                .histogram()
                .buckets().array()
                .get(0)
                .docCount();

        System.out.println("444444444444444444444444");
        System.out.println("doc count: " + firstBucketCount);
    }
}
