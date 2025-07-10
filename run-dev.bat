@echo off
echo Starting FertiCare Backend with DevTools...
echo.
echo Hot reload is enabled - changes will be automatically detected!
echo.
echo Available endpoints:
echo - API Documentation: http://localhost:8080/swagger-ui/index.html
echo - H2 Database Console: http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the server
echo.

mvn spring-boot:run -Dspring-boot.run.profiles=dev 