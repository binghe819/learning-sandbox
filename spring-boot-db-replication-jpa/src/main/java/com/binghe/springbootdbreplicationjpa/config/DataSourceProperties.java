package com.binghe.springbootdbreplicationjpa.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Replication에 사용 될 DataSource 설정 객체
 * (Profile에 명시된 내용을 가져온다.)
 */
// 빈으로 등록된 Configuration에서 @EnableConfigurationProperties 혹은 @ConfigurationPropertiesScan를 통해 스캔해야 한다.
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {

    private Map<String, DatasourceNode> slave = new HashMap<>();
    private DatasourceNode master;

    public Map<String, DatasourceNode> getSlave() {
        return slave;
    }

    public DatasourceNode getMaster() {
        return master;
    }

    public void setMaster(DatasourceNode master) {
        this.master = master;
    }

    // DatasourceNode (요소) - Master DB 혹은 Slave DB의 정보를 담고 있는 객체
    public static class DatasourceNode {
        private String name;
        private String url;
        private String username;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
