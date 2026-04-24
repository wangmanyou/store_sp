package org.example.store_sp_backend.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private Long total;
    private Long pageNum;
    private Long pageSize;
    private List<T> list;

    public static <T> PageResponse<T> from(IPage<T> page) {
        return new PageResponse<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }
}
