package paas.rey.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yeyc
 * @Description MybatisPlus分页插件配置
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Configuration
public class MybatisPlusPageConfig {
           /*  旧版本配置
      @Bean
      public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
      }*/

        /**
         * 新的分页插件,一缓和二缓遵循mybatis的规则,
         * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
         */
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor() {
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
            return interceptor;
        }
}
