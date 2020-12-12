# Bug example for [spring-cloud/spring-cloud-sleuth#1795][1]

|Test scenario|Expected|Actual|Pass|
|---|---|---|---|
|Conditional Bean/Sleuth Enabled|Find 1 `ClientInterceptor`|Finds 0 `ClientInterceptor`s|No|
|Conditional Bean/Sleuth Disabled|Find 0 `ClientInterceptor`s|Finds 0 `ClientInterceptor`s|Yes|
|Optional Injection/Sleuth Enabled|Find 1 `ClientInterceptor`|Finds 1 `ClientInterceptor`|Yes|
|Optional Injection/Sleuth Disabled|Find 0 `ClientInterceptor`s|Finds 0 `ClientInterceptor`s|Yes|
|Unconditional Bean/Sleuth Enabled|Find 1 `ClientInterceptor`|Finds 1 `ClientInterceptor`|Application behaves ok, test fails|
|Unconditional Bean/Sleuth Disabled|Throw because of missing bean|Throw because of missing bean|Yes|

Check [GrpcConfigurationTest][2] for automated tests to prove the above.

[1]: https://github.com/spring-cloud/spring-cloud-sleuth/issues/1795

[2]: https://github.com/dimi-nk/spring-cloud-sleuth-1795-example/blob/master/src/test/java/com/example/GrpcConfigurationTest.java
