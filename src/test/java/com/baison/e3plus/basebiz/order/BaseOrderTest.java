package com.baison.e3plus.basebiz.order;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@Transactional
@Rollback
public abstract class BaseOrderTest extends BaseMockBean{
	
	public static String testToken  = "admin";

	@Before
	public void initMocks(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(bs2RedisPool.getSource()).thenReturn(jedis);
	}

}
