import com.leyou.DTO.SpuDTO;
import com.leyou.ItemClient;
import com.leyou.LyPageApplication;
import com.leyou.exception.vo.PageResult;
import com.leyou.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyPageApplication.class)
public class PageServiceTest {
    @Autowired
    private ItemClient itemClient;
    @Autowired
    private PageService pageService;

    @Test
    public void createItemHtml() throws InterruptedException {

        /*Long[] arr = {96L, 114L, 124L, 125L, 141L};
        for (Long id : arr) {
            pageService.createItemHtml(id);
            Thread.sleep(500);
        }*/
        int page = 1;
        int size = 10;
        do {
            try {
                PageResult<SpuDTO> pageResult = itemClient.querySpuByPage(page, size,true,null);
                //处理返回SpuDTO对象
                List<SpuDTO> items = pageResult.getItems();
                for (SpuDTO item : items) {
                    pageService.createItemHtml(item.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            page++;
        }while (page<=20);

    }
}
