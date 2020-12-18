# 实体

<td valign="top">

## 券模板

| 类别    | 字段          | 含义    | 说明 |
| :----: | ------------ | ------- |--- |
| 优惠规则 | customer     | 适用用户 |
| ~      | rule         | 优惠规则 |
| ~      | discount     | 折扣     |
| 分发信息 | distribute   | 分发方式 |
| ~      | product_line | 产品线   |
| ~      | coupon_count | 总数    |
| ~      | available    | 是否过期 |
| ~      | expired      | 总数    |
| 展示信息 | coupon_name  | 名称    |
| ~      | logo         | logo    |
| ~      | intro        | 介绍     |
| 创建信息 | id           | 自增主键 |
| ~      | sn           | 模板编码 |
| ~      | create_time  | 创建时间 |
| ~      | user_id      | 创建人   |

</td>


<td valign="top">

## 优惠券

| 字段           | 含义         | 说明 |
|--------------|------------|----|
| coupon\_code | 优惠券码       |    |
| status       | 优惠券的状态     |    |
| assign\_time | 领取时间       |    |
| user\_id     | 领取用户       |    |
| template\_id | 关联优惠券模板的主键 |    |
| id           | 自增主键       |    |

</td>

# template 优惠券模板 微服务
主要功能：
1. 根据运营给定的参数生成模板，并返回模板信息
2. 根据模板（提前静态）生成优惠券（由功能1返回前调用）
3. 清理过期的优惠券

功能1：（BuildTemplateServiceImpl）
1. 请求参数校验
2. 从MySQL中查询是否已有该模板
3. TemplateRequestVo -> CouponTemplate
4. CouponTemplate存储数据库，并获得具有映射关系的返回
5. 调用功能2，异步生成优惠券码
6. 返回结果

功能2：（BuildCouponServiceImpl）
1. 生成所有的优惠券码，并将其依次放入Redis中的一个List中
2. 更改优惠券模板的可用状态

券码（18位）：产品线序号（2位）+ 折扣方式（2位）+ 模板创建日期（6位，yyMMdd）+ 随机数（8位）

# distribution 分发 微服务
主要功能
1. 查找用户优惠券（根据用户id和优惠券状态）
2. 查找用户可以领取的优惠券模板（根据用户id）
3. 用户领取优惠券
4. 结算/核销优惠券
> - 结算：计算优惠券可以优惠的金额，但不付款
> - 核销：实际使用优惠券

功能1：