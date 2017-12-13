package br.ufpe.cin.if710.podcast.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlpull.v1.XmlPullParser;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by tulio on 12/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlFeedParserTest {

    @Mock
    XmlPullParser parser;


    @Test
    public void readRss() throws Exception {


        String title = "Darwin e a Evolução";
        String guid = "http://frontdaciencia.ufrgs.br/#3";
        String pubDate = "Mon, 21 Jun 2010 10:45:05 GMT";
        String description = "Programa 3";
        String link = "http://frontdaciencia.ufrgs.br/#3";

        String url = "http://dstats.net/download/http://www6.ufrgs.br/frontdaciencia/arquivos/Fronteiras_da_Ciencia-E003-Darwin_e_a_Evolucao-25.09.2009.mp3";
        String lenght = "26374169";
        String type = "audio/mpeg";

        ItemFeed item = new ItemFeed(title,link,pubDate,description,url,"");

        when(parser.getText()).thenReturn(title).thenReturn(link).thenReturn(guid).thenReturn(pubDate).thenReturn(description);

        when(parser.getAttributeValue(null,"url")).thenReturn(url);
        when(parser.getAttributeValue(null,"lenght")).thenReturn(lenght);
        when(parser.getAttributeValue(null,"type")).thenReturn(type);

        when(parser.next()).thenReturn(XmlPullParser.START_TAG)
                .thenReturn(XmlPullParser.START_TAG)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.END_TAG)
                .thenReturn(XmlPullParser.END_TAG)
                .thenReturn(XmlPullParser.END_TAG);

        when(parser.getName()).thenReturn("channel").thenReturn("item").thenReturn("title").thenReturn("link").thenReturn("guid").thenReturn("pubDate").thenReturn("description").thenReturn("enclosure").thenReturn("skip");

        when(parser.getEventType()).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG);
        List<ItemFeed>retrieved =XmlFeedParser.readRss(parser);

        assertEquals(item.equals(retrieved.get(0)),true);

    }

    @Test
    public void readChannel() throws Exception {

        String title = "Darwin e a Evolução";
        String guid = "http://frontdaciencia.ufrgs.br/#3";
        String pubDate = "Mon, 21 Jun 2010 10:45:05 GMT";
        String description = "Programa 3";
        String link = "http://frontdaciencia.ufrgs.br/#3";

        String url = "http://dstats.net/download/http://www6.ufrgs.br/frontdaciencia/arquivos/Fronteiras_da_Ciencia-E003-Darwin_e_a_Evolucao-25.09.2009.mp3";
        String lenght = "26374169";
        String type = "audio/mpeg";

        ItemFeed item = new ItemFeed(title,link,pubDate,description,url,"");

        when(parser.getText()).thenReturn(title).thenReturn(link).thenReturn(guid).thenReturn(pubDate).thenReturn(description);

        when(parser.getAttributeValue(null,"url")).thenReturn(url);
        when(parser.getAttributeValue(null,"lenght")).thenReturn(lenght);
        when(parser.getAttributeValue(null,"type")).thenReturn(type);

        when(parser.next()).thenReturn(XmlPullParser.START_TAG)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.END_TAG)
                .thenReturn(XmlPullParser.END_TAG);

        when(parser.getName()).thenReturn("item").thenReturn("title").thenReturn("link").thenReturn("guid").thenReturn("pubDate").thenReturn("description").thenReturn("enclosure").thenReturn("skip");

        when(parser.getEventType()).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG);
        List<ItemFeed>retrieved =XmlFeedParser.readChannel(parser);

        assertEquals(item.equals(retrieved.get(0)),true);

    }

    @Test
    public void readItem() throws Exception {
        String title = "Darwin e a Evolução";
        String guid = "http://frontdaciencia.ufrgs.br/#3";
        String pubDate = "Mon, 21 Jun 2010 10:45:05 GMT";
        String description = "Programa 3";
        String link = "http://frontdaciencia.ufrgs.br/#3";

        String url = "http://dstats.net/download/http://www6.ufrgs.br/frontdaciencia/arquivos/Fronteiras_da_Ciencia-E003-Darwin_e_a_Evolucao-25.09.2009.mp3";
        String lenght = "26374169";
        String type = "audio/mpeg";

        ItemFeed item = new ItemFeed(title,link,pubDate,description,url,"");
        when(parser.next()).thenReturn(XmlPullParser.TEXT);
        when(parser.getText()).thenReturn(title).thenReturn(link).thenReturn(guid).thenReturn(pubDate).thenReturn(description);

        when(parser.getAttributeValue(null,"url")).thenReturn(url);
        when(parser.getAttributeValue(null,"lenght")).thenReturn(lenght);
        when(parser.getAttributeValue(null,"type")).thenReturn(type);

        when(parser.next()).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.TEXT)
                .thenReturn(XmlPullParser.END_TAG);
        when(parser.getName()).thenReturn("title").thenReturn("link").thenReturn("guid").thenReturn("pubDate").thenReturn("description").thenReturn("enclosure").thenReturn("skip");
        when(parser.getEventType()).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG).thenReturn(XmlPullParser.START_TAG);
        ItemFeed retrieved =XmlFeedParser.readItem(parser);

        assertEquals(item.equals(retrieved),true);

    }

    @Test
    public void readData() throws Exception {

        String text = "Darwin e a Evolução";
        String tag = "title";
        when(parser.next()).thenReturn(XmlPullParser.TEXT);
        when(parser.getText()).thenReturn(text);
        assertEquals(XmlFeedParser.readData(parser,tag),text);


    }

    @Test
    public void readText() throws Exception {
        String text = "text";
        when(parser.next()).thenReturn(XmlPullParser.TEXT);
        when(parser.getText()).thenReturn(text);

        assertEquals(XmlFeedParser.readText(parser),text);
    }

    @Test
    public void readEnclosure() throws Exception {

        String url = "http://dstats.net/download/http://www6.ufrgs.br/frontdaciencia/arquivos/Fronteiras_da_Ciencia-E003-Darwin_e_a_Evolucao-25.09.2009.mp3";
        String lenght = "26374169";
        String type = "audio/mpeg";


        when(parser.getAttributeValue(null,"url")).thenReturn(url);
        when(parser.getAttributeValue(null,"lenght")).thenReturn(lenght);
        when(parser.getAttributeValue(null,"type")).thenReturn(type);

        assertEquals(XmlFeedParser.readEnclosure(parser),url);

    }

}