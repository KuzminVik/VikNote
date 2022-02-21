# VikNote
### Описание
VikNote - приложение для Android, написано на языке Kotlin в 2021 году во время практических занятий на факультете андроид-разработки университета Geekbrains.
VikNote представляет собой простой блокнот, позволяющий сохранять текстовые заметки в памяти смартфона, распределять их по разныи категориям, редактировать, удалять и присваивать закладки разных цветов.

Стек технологий - Kotlin, MVVM и MVI, ViewModel и LiveData, Room, Coroutines, Koin, SharedPreferences. 

Внимание! Дизайн не является сильной стороной данного приложения!

![alt text](screenshots/Screenshot_20220104_232837.png "Список папок")
![alt text](screenshots/Screenshot_20220104_232954.png "Список заметок и раскрытый fab")

### Архитектура
В приложении реализованы две архитектурные модели - MVVM и MVI. В соответствии с подходом сингл-активити, приложение имеет одну базовую активити, которая создает тулбар, кнопки FAB и фрейм-контейнер для отображения фрагментов. Четыре фрагмента представляют собой экраны: список папок, список заметок в папке, режим редактирования, режим детального просмотра. Каждый фрагмент имеет свой класс ViewModel для разделения слев отображения и бизнес-логики. Вьюмодель сохраняет данные для фрагмента в виде LiveData, на которую тот подписывается. Имеется и одна Shared ViewModel для того, чтобы фрагменты могли сообщать активити, какой набор кнопок FAB с какими listeners должны быть отображены в данный момент. 

Вклад MVI состоит в том, что для управлением пользовательского интерфейса реализованы три State Machines на основе sealed классов. Больше в учебных целях, чем по необходимости. Первый, AppState, описывает состояния получения и отображения данных всех экранов, кроме режима редактирования. Второй, EditingModeState, используется с анологичными целями, только для экрана редактирования, о чем подробнее будет сказано ниже. Частично управление сотояними экранов возложено и на данные, сохраняемы и получаемые через SharedPreferences и список констант. Третий sealed класс FabState описывает состояния (вид и назначение) кнопок FAB в активити.

![alt text](screenshots/Screenshot_20220104_232941.png "Детальный просмотр заметки и раскрытый fab")
![alt text](screenshots/Screenshot_20220104_233046.png "Добавить новую заметку")
![alt text](screenshots/Screenshot_20220104_233117.png "Отредактировано существующую заметку")

### База данных и ассинхронность
В приложении используется компонент Room, управляющий встроенным в андроид SQLite. Два Entity-класса описывают модель данных, для каждого есть свой интерфейс Dao с методами запросов к базе данных. Класс DataBaseImpl является посредником для передачи данных в вьюмодель. Все запросы и ответы выполняются асинхронно при помощи корутин, которые запускаются в соответствующих ViewModel. 

### Dependency Injection
В качестве компонента для внедрения зависимостей используется Koin. Инжектятся: инстанс базы данных, интерфейсы Dao и сам DataBaseImpl, все вьюмодели. Так же вынесен в отдельный класс и инжектится SharedPreferences, потому что использует в своей работе контекст. Кроме того для удобного хранения и смены состояний фишек в ChipsGroup создан отдельный класс, где мапятся все id chips, он тоже инжектится во вьюмодель экрана редактирования.

### Swipe
Экраны списка папок и списка заметок используют RecyclerView и реализуют свайп при помощи ItemTouchHelper.SimpleCallback. Свайп влево запускают редактирование элемента, а вправо - его удаление. В реализации использован проект пользователя гитхаба xabaras под названием RecyclerViewSwipeDecorator, переведенный на Котлин и слегка адаптированный для моего приложения.

### LiveSearch
На экранах списка папок и списка заметок реализован "живой поиск" - ввод текста в поисковой строке мгновенно обновляет список, так что в нем остаются только те элементы, что удовлетворяют поисковому звпросу. Используется объект класса Filter в адаптере ресайклервью. 

### Spinner
На экране редактирования заметки реализован выпадающий список с имеющимися в данный момент папками. Соответственно, заметку можно переложить из одной папки в другую.

### EditingModeState
Отдельно стоит сказать про управление состояниями пользовательского интерфейса на экране редактирования. Этот фрагмент своего рода монстр, конечно, исходя из принципа единой ответственности, его следовало бы разбить минимум на два фрагмента. Но он намеренно оставлен в таком виде для экспериментаторских целей. Экран редактирования имеет четыре уровня UI в xml-layout - создание новой папки, создание новой заметки, редактирование существующей папки и редактирование существующей заметки. Соответственно всем этим управляют константы, SharedPreferences и sealed класс EditingModeState.

### И, кстати
Если у вас есть дельные замечания, поправки, буду признателен за ваши сообщения на https://t.me/VikSimurg