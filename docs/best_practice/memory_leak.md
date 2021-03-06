[Главная](../main.md)

[TOC]

# Обнаружение утечек памяти

[Хорошая презентация на тему][pres]

Утечки памяти являются не частыми проблемами, однако они могут быть причиной
больших затрат времени и нервов, требуемых для их исправления. В этой статье
описан ряд советов, как можно решить эту проблему быстрее.

Утечки Activity, Fragment, View можно отследить с помощью библиотеки **LeakCanary**.
Также утечки активити можно искать с помощью правой вкладке (**Analyzer Tasks**)
просмотрщика Heap Dump (**HPROF Viewer and Analyzer**). Как снять Heap Dump описано ниже.

Для других утечек можно использовать комбинацию 3-х основных инструментов:
* Memory Monitor в AndroidStudio во вкладке Monitors
* HPROF Viewer and Analyzer
* Примерная локализация утечки


### Memory Monitor

![](https://image.ibb.co/mJOJHK/leak_monitor.png)

C помощью этого инструмента можно находить те сценарии, в результате которых происходит
утечка памяти. Для чистоты эксперимента лучше после такого сценария запускать сборщик
мусора принудительно (с помощью соответствующей кнопки монитора). Когда такой
сценарий был найден, то можно снять Heap Dump (все java обьекты, которые были в
виртуальной машине) в начале и конце сценария для дальнейшего анализа. Пред снятием
Heap Dump необходимо принудительно запустить сборщик мусора. Также можно снять
Memory allocation.

Запуск сбора этой информации осуществляется через соответствующие кнопки монитора.

### HPROF Viewer and Analyzer

![](https://preview.ibb.co/jW87Be/leak_viewer.png)

Позволяет просматривать Heap Dump.

Левый верхний блок
* Total - всего обьектов определенного типа
* Shallow Size - сколько занимают в памяти все эти обьекты без учета вложенных обьектов
* Retained Size - сколько занимают в памяти все эти обьекты с учетом вложенных обьектов

Правый верхний блок
* Детализирует запись из левого блока. Показывает все инстансы с их потрохами.

Нижний блок
* Показывает дерево ссылок на обьект, выбранный в правом блоке.

### Анализ данных

Сравнивая Heap Dump до и после выполнения сценария с утечкой памяти,
можно обнаружить искомые нам обьекты по разнице их количества. Нижний
блок покажет, какие конкретно обьекты держат сслыки на найденные обьекты
и не позволяют собрать их gc.
Если первый анализ не дал результатов, то нужно найти те строки кода,
выполнение которых вызывает утечку и снять Heap Dump’ы после двух аналогичных
сценариев, только в одном из них следует не выполнять упомянутые выше строки кода.
Сравнение этих дампов позволит точно найти какие обьекты утекают. Помочь найти
такие строки кода могут первые 2 инструмента, также можно комментировать вызовы
ключевых методов сценария и проверять выделение пямяти.

[pres]: https://docs.google.com/presentation/d/1M7liGSO8oljFff5tZkK0CaosZPNum1t7zXHyt0dAxzg/edit#slide=id.p



