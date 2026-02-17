# 🗺️ SKILL.md: Modernização UI/UX para FreeMind

Este guia define as competências e passos técnicos necessários para transformar a interface legada do FreeMind em uma experiência desktop moderna e fluida em 2026.

## 1. Modernização Visual do Swing (Curto Prazo)
O FreeMind utiliza a biblioteca Swing. O primeiro passo é abandonar os "Look and Feels" nativos datados (como Metal ou Motif).
*   **Domínio do [FlatLaf](https://www.formdev.com):** Implementar o FlatLaf para habilitar temas **Light**, **Dark** e suporte total a telas **HiDPI/Retina**.
*   **Customização de Bordas e Espaçamento:** Substituir bordas sólidas por sombras suaves e aumentar o *padding* interno de painéis e botões para uma estética "limpa".
*   **SVG Icons:** Migrar ícones rasterizados (.png/.gif) para ícones vetoriais usando [FlatLaf SVG Support](https://www.formdev.comsvg-icons/) para garantir nitidez em qualquer nível de zoom.

## 2. Refatoração de Arquitetura UI
Para melhorar a manutenção, é necessário desacoplar a lógica do mapa mental da sua representação visual.
*   **Padrão MVC/MVP:** Refatorar as classes `MapView` e `NodeView` para que a lógica de manipulação de dados não dependa diretamente de componentes Swing.
*   **Injeção de Dependências:** Utilizar frameworks leves para gerenciar o estado da UI, facilitando testes unitários de componentes visuais.

## 3. Transição para Paradigmas Modernos (Longo Prazo)
Preparar o código para uma eventual migração tecnológica.
*   **[JavaFX](https://openjfx.io/):** Habilidade em integrar componentes JavaFX dentro do Swing via `JFXPanel`. Isso permite usar animações complexas e estilização via **CSS** em partes específicas do mapa.
*   **Compose for Desktop (Kotlin/Java):** Entender o modelo **declarativo**. Se o core do FreeMind for desacoplado, o Compose pode renderizar o mapa mental com performance superior usando a engine Skia.

## 4. Princípios de UX Aplicados a Mapas Mentais
*   **Micro-interações:** Implementar animações suaves ao expandir/recolher nós para reduzir a carga cognitiva.
*   **Acessibilidade (A11y):** Garantir que todos os comandos do mapa sejam navegáveis via teclado e compatíveis com leitores de tela.
*   **Busca Semântica na UI:** Integrar componentes de busca que ofereçam feedback visual instantâneo no mapa conforme o usuário digita.

## 🚀 Como Começar
1.  **Analise o `build.xml`:** O FreeMind usa Ant para build. Aprenda a incluir novas dependências .jar no classpath.
2.  **Prototipe no Figma:** Antes de codar, desenhe a nova barra de ferramentas e o estilo dos nós no Figma para validar o fluxo visual.
3.  **Teste de LookAndFeel:** Adicione `FlatLightLaf.setup();` no método `main` para ver a transformação imediata.