import com.leyou.LyPageApplication;
import com.leyou.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyPageApplication.class)
public class PageServiceTest {

    @Autowired
    private PageService pageService;

    @Test
    public void createItemHtml() throws InterruptedException {
        Long[] arr = {96L, 114L, 124L, 125L, 141L};
        for (Long id : arr) {
            pageService.createItemHtml(id);
            Thread.sleep(500);
        }
    }
}
