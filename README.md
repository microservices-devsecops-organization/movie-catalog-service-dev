<!-- [![Docker hub](https://badgen.net/badge//Docker%20Hub?icon=docker)](https://hub.docker.com/r/renatomefi/docker-testinfra/)
[![Docker hub](https://img.shields.io/docker/pulls/renatomefi/docker-testinfra.svg)](https://hub.docker.com/r/renatomefi/docker-testinfra/)
[![Docker hub](https://img.shields.io/microbadger/image-size/renatomefi/docker-testinfra/2.svg)](https://hub.docker.com/r/renatomefi/docker-testinfra/) -->


# movie-catalog-service

[Artigo sobre o padrão de arquitetura técnica](http://coinova.claro.com.br/arqtec/poc/)

Main technologies and platforms used are:
* Java, Spring Boot, Spring Cloud, Resilience4J, Spring Cloud Kubernetes, RX Java, Micrometer
* Docker, Kubernetes, Prometheus, Grafana, Elastic Search, FluentD, Kibana, Jaeger, Istio, Helm, Kops
* Jenkins, Git, Nexus, Harbor, SonarQube, Maven, Junit, FindSecBugs, OWASP DC

### Overview
![](http://coinova.claro.com.br/wp-content/uploads/2020/04/poc.png "")

### Pipeline DevSecOps
![](http://coinova.claro.com.br/wp-content/uploads/2020/04/pipeline.png "")

## Release notes:
### Version 1.2.0
- Update da versão do Spring Boot para 2.2.5.RELEASE 
- Vulnerabilidade CVE-2020-1938 corrigida através do update da versão do Spring Boot acima.
- Atualizado com a implementação do conceito de sidecar. Adicionado container sidecar para acesso ao sistema legado de fatura. 
- Atualizado para utilizar as novas versões das Shared Libs do pipeline de DevSecOps
- Alterado apiVersion do ingress do k8s para networking.k8s.io/v1beta1
- Criado novo kustomization sem utilização de commonLabels devido ao impacto na criação de labels em selector do service para endpoint de serviços externos ao cluster k8s.
- Vulnerabilidade CVE-2020-8552 referente a versão do Kubernetes 1.14.5. Vulnerabilidade surpimida no owasp-dependency-check-supression.xml até 31/12/2020. Maiores detalhes em https://nvd.nist.gov/vuln/detail/CVE-2020-8552
- Atualização da data de supressão da CVE-2019-11253 referente a versão do Kubernetes 1.14.5. até 31/12/2020.
- Atualizado Jenkinsfile para suportar os passos necessários para o container sidecar
- Para contemplar a utilização de sidecar várias sharedlibs utilizadas no pipeline de DevSecOps foram atualizadas.

### Version 1.1.0
- Atualizada imagem Docker para azul/zulu-openjdk-alpine:11-jre
- Update da versão do Spring Boot para 2.2.3.RELEASE
- Update da versão do Spring Cloud para Hoxton.RELEASE
- Atualizadas classes conectors e services para receber via parametrização o correlationId devido ao Autowired utilizar o conceito de singleton. Quando esses valores eram salvos vias propriedades globais de suas classes os mesmos eram sobrescritos a cada nova requisição.
- Adicionado context-path em bootstrap.yaml com o nome do microserviço para todos os endpoints expostos. 
- Atualizados endpoints dos arquivos do k8s (ingress, service-monitor, deployment) para contemplar o context-path do microserviço.
- Adicionada seção de images em kustomization.yaml para configurar o nome da imagem correta durante pipeline DevSecOps.
- Removida dependência da biblioteca esapi (org.owasp.esapi) devido a vulnerabilidade CVE-2019-10086. Está lib não estava sendo utilizada e estava no código como uma possível biblioteca utilitária.
- Vulnerabilidade CVE-2019-11253 referenrte a versão do Kubernetes 1.14.5. E-mail enviado para time de cloud e segurança referente a roadmap do Kubernetes. Vulnerabilidade surpimida no owasp-dependency-check-supression.xml até 31/03/2020.
- Upgrade da biblioteca do resilience4j para 1.1.0
- Remoção de fileappenders do logback-spring.xml. Utilizado somente appender que será capturado pelo FluentD na suite EFK.
