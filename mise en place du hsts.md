ajout pour site hsts

```bash
Header always set Strict-Transport-Security "max-age=86400"
# Header always set Strict-Transport-Security "max-age=31536000; includeSubDomains"
```

```bash
stefdev@ubuntu:/etc/apache2/sites-available$ sudo nano loto-tracker.fr.conf
[sudo] password for stefdev:
stefdev@ubuntu:/etc/apache2/sites-available$ sudo apache2ctl configtest
AH00558: apache2: Could not reliably determine the server's fully qualified domain name, using 82.165.185.36. Set the 'ServerName' directive globally to suppress this message
Syntax OK
stefdev@ubuntu:/etc/apache2/sites-available$ sudo systemctl reload apache2
stefdev@ubuntu:/etc/apache2/sites-available$ curl -I https://loto-tracker.fr | grep -i strict
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
strict-transport-security: max-age=86400
stefdev@ubuntu:/etc/apache2/sites-available$ curl -I http://loto-tracker.fr
curl -I http://www.loto-tracker.fr
HTTP/1.1 301 Moved Permanently
Date: Sun, 14 Jun 2026 16:25:48 GMT
Content-Type: text/html; charset=iso-8859-1
Connection: keep-alive
Server: cloudflare
Location: https://loto-tracker.fr/
Nel: {"report_to":"cf-nel","success_fraction":0.0,"max_age":604800}
cf-cache-status: DYNAMIC
Report-To: {"group":"cf-nel","max_age":604800,"endpoints":[{"url":"https://a.nel.cloudflare.com/report/v4?s=OHCioZcxxCSYX6DTVhTMEDzRzatTEQtZhmcQhw64besa4Wa07s%2BFBwmSylBOYQmAdt5rO8b%2F8UMYcpQjmQSTlUgvglKj99nsJDGAbNyz9ks2vqt9UyO4d5cDT%2B6%2FP9CTZtI%3D"}]}
CF-RAY: a0bab16b6856d2be-FRA
alt-svc: h3=":443"; ma=86400

HTTP/1.1 301 Moved Permanently
Date: Sun, 14 Jun 2026 16:25:48 GMT
Content-Type: text/html; charset=iso-8859-1
Connection: keep-alive
Server: cloudflare
Location: https://loto-tracker.fr/
Nel: {"report_to":"cf-nel","success_fraction":0.0,"max_age":604800}
cf-cache-status: DYNAMIC
Report-To: {"group":"cf-nel","max_age":604800,"endpoints":[{"url":"https://a.nel.cloudflare.com/report/v4?s=rK5pRSJRX9L4DpF4m1o65KYb8XyH6nQU59AsGhkHrlgRRgKBpwi%2FtsJ8BxjQboSGZnKYpeGCPUTOFcJenPQ6VqzeRndFwnKX4Pp5SaX8lyiHexoso%2BFgOz16rdO%2FtTzDMS0gk4ef"}]}
CF-RAY: a0bab16bdc3c71b9-FRA
alt-svc: h3=":443"; ma=86400

stefdev@ubuntu:/etc/apache2/sites-available$
```
