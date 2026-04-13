// data/init/InitialDataLoader.kt
package com.example.myhealthydiet.data.init

import com.example.myhealthydiet.R
import com.example.myhealthydiet.data.local.room.entities.*

object InitialDataLoader {

    fun getProductCategories(): List<ProductCategoryEntity> {
        return listOf(
            ProductCategoryEntity(1, "Молочные продукты", "ic_dairy"),
            ProductCategoryEntity(2, "Мясо и птица", "ic_meat"),
            ProductCategoryEntity(3, "Рыба и морепродукты", "ic_fish"),
            ProductCategoryEntity(4, "Яйца", "ic_eggs"),
            ProductCategoryEntity(5, "Овощи", "ic_vegetables"),
            ProductCategoryEntity(6, "Фрукты и ягоды", "ic_fruits"),
            ProductCategoryEntity(7, "Крупы и каши", "ic_grains"),
            ProductCategoryEntity(8, "Хлеб и выпечка", "ic_bread"),
            ProductCategoryEntity(9, "Макаронные изделия", "ic_pasta"),
            ProductCategoryEntity(10, "Орехи и семена", "ic_nuts"),
            ProductCategoryEntity(11, "Масла и жиры", "ic_oils"),
            ProductCategoryEntity(12, "Сладости и десерты", "ic_sweets"),
            ProductCategoryEntity(13, "Напитки", "ic_drinks"),
            ProductCategoryEntity(14, "Соусы и приправы", "ic_sauces"),
            ProductCategoryEntity(15, "Другое", "ic_other")
        )
    }

    fun getDishCategories(): List<DishCategoryEntity> {
        return listOf(
            DishCategoryEntity(
                1,
                "Завтраки",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat6}"
            ),
            DishCategoryEntity(
                2,
                "Первые блюда",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat4}"
            ),
            DishCategoryEntity(
                3,
                "Вторые блюда",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat2}"
            ),
            DishCategoryEntity(
                4,
                "Салаты",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat3}"
            ),
            DishCategoryEntity(
                5,
                "Десерты",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat1}"
            ),
            DishCategoryEntity(
                6,
                "Напитки",
                "android.resource://com.example.myhealthydiet/${R.drawable.kat5}"
            )
        )
    }

    fun getStandardProducts(): List<ProductEntity> {
        return listOf(
            ProductEntity(0, 2, "Куриная грудка жареная", 180, 23, 18, 1, false),
            ProductEntity(0, 2, "Говядина отварная", 254, 26, 17, 0, false),
            ProductEntity(0, 2, "Бекон жареный", 468, 34, 35, 2, false),
            ProductEntity(0, 1, "Молоко 3,2%", 60, 3, 3, 5, false),
            ProductEntity(0, 1, "Сыр Голландский", 332, 26, 25, 0, false),
            ProductEntity(0, 1, "Творог 9%", 141, 12, 9, 3, false),
            ProductEntity(0, 7, "Горох", 298, 21, 2, 50, false),
            ProductEntity(0, 7, "Фасоль", 298, 21, 2, 47, false),
            ProductEntity(0, 8, "Хлеб дарницкий", 206, 7, 1, 41, false),
            ProductEntity(0, 8, "Батон", 259, 8, 3, 50, false),
            ProductEntity(0, 3, "Горбуша жареная", 175, 20, 10, 2, false),
            ProductEntity(0, 5, "Картофель жареный", 148, 3, 5, 22, false),
            ProductEntity(0, 6, "Банан", 96, 2, 1, 21, false),
            ProductEntity(0, 6, "Яблоко", 47, 0, 0, 10, false),
            ProductEntity(0, 7, "Гречка", 320, 14, 3, 58, false),
            ProductEntity(0, 7, "Рис круглозерный", 330, 7, 1, 74, false),
            ProductEntity(0, 7, "Овсянка", 360, 11, 6, 66, false),
            ProductEntity(0, 12, "Печенье «Юбилейное»", 463, 7, 18, 67, false),
            ProductEntity(0, 13, "Кола «Добрый»", 42, 0, 0, 11, false),
            ProductEntity(0, 9, "Макароны", 338, 11, 1, 71, false),
            ProductEntity(0, 10, "Арахис", 552, 26, 45, 10, false),
            ProductEntity(0, 10, "Миндаль", 609, 19, 54, 13, false),
            ProductEntity(0, 11, "Масло подсолнечное", 899, 0, 99, 0, false)
        )
    }

    fun getStandardDishes(): List<DishEntity> {
        return listOf(
            // Завтраки
            DishEntity(
                id = 0, categoryId = 1, name = "Сырники с творогом", minutesToCook = 20,
                ingredients = """[{"name":"Творог","amount":"200 г"},{"name":"Яйцо","amount":"1 шт"},{"name":"Сахар","amount":"2 ст. ложки"},{"name":"Мука","amount":"3 ст. ложки"},{"name":"Масло растительное","amount":"2 ст. ложки"}]""",
                steps = "Шаг 1: Смешайте творог, яйцо и сахар в глубокой миске.\nШаг 2: Постепенно добавляйте муку, пока не получится густое тесто.\nШаг 3: Сформируйте небольшие лепешки из теста.\nШаг 4: Разогрейте сковороду с маслом на среднем огне.\nШаг 5: Обжарьте сырники с обеих сторон до золотистой корочки, примерно по 3-4 минуты с каждой стороны.\nШаг 6: Подавайте горячими с мёдом, вареньем или сметаной.",
                calories = 234, proteins = 13, fats = 9, carbs = 12,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe1}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 1, name = "Панкейки с мёдом и ягодами", minutesToCook = 15,
                ingredients = """[{"name":"Мука","amount":"150 г"},{"name":"Яйцо","amount":"1 шт"},{"name":"Молоко","amount":"200 мл"},{"name":"Сахар","amount":"1 ст. ложка"},{"name":"Разрыхлитель","amount":"1 ч. ложка"},{"name":"Соль","amount":"щепотка"}]""",
                steps = "Шаг 1: В миске смешайте муку, сахар, разрыхлитель и соль.\nШаг 2: В другой миске взбейте яйцо и молоко.\nШаг 3: Соедините сухие и жидкие ингредиенты и перемешайте до однородной массы.\nШаг 4: Разогрейте сковороду с небольшим количеством масла.\nШаг 5: Выложите тесто на сковороду порциями, формируя небольшие панкейки.\nШаг 6: Обжаривайте по 2 минуты с каждой стороны до золотистой корочки.\nШаг 7: Подавайте с мёдом и ягодами.",
                calories = 220, proteins = 6, fats = 8, carbs = 32,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe2}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 1, name = "Овсянка с ягодами", minutesToCook = 15,
                ingredients = """[{"name":"Овсяные хлопья","amount":"100 г"},{"name":"Молоко","amount":"200 мл"},{"name":"Ягоды (клубника, малина)","amount":"50 г"},{"name":"Мёд","amount":"1 ст. ложка"}]""",
                steps = "Шаг 1: Налейте молоко в кастрюлю и доведите до кипения на среднем огне.\nШаг 2: Добавьте овсяные хлопья в кипящее молоко и хорошо перемешайте.\nШаг 3: Варите овсянку, помешивая, на слабом огне около 5-7 минут, пока она не загустеет.\nШаг 4: Снимите кастрюлю с огня и дайте овсянке настояться пару минут.\nШаг 5: Добавьте мёд и перемешайте. Разложите овсянку по тарелкам.\nШаг 6: Выложите свежие ягоды на овсянку сверху.\nШаг 7: Подавайте горячей, при желании можно украсить мятой.",
                calories = 130, proteins = 5, fats = 3, carbs = 22,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe3}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 1, name = "Яичница с беконом", minutesToCook = 10,
                ingredients = """[{"name":"Яйца","amount":"2 шт"},{"name":"Бекон","amount":"50 г"},{"name":"Соль, перец","amount":"по вкусу"}]""",
                steps = "Шаг 1: Разогрейте сковороду на среднем огне.\nШаг 2: Выложите ломтики бекона на сковороду и обжаривайте их 2-3 минуты с каждой стороны до золотистой корочки.\nШаг 3: Снимите бекон со сковороды и выложите на бумажное полотенце, чтобы удалить лишний жир.\nШаг 4: В ту же сковороду аккуратно разбейте яйца, посолите и поперчите по вкусу.\nШаг 5: Жарьте яйца 3-4 минуты, пока белки не станут полностью прозрачными.\nШаг 6: Переложите яичницу на тарелку и украсьте сверху ломтиками бекона.\nШаг 7: Подавайте сразу, можно дополнить зеленью или тостами.",
                calories = 270, proteins = 18, fats = 20, carbs = 1,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe4}",
                isCustom = false
            ),

            // Первые блюда
            DishEntity(
                id = 0, categoryId = 2, name = "Борщ с говядиной", minutesToCook = 120,
                ingredients = """[{"name":"Говядина","amount":"500 г"},{"name":"Свёкла","amount":"1 шт"},{"name":"Картофель","amount":"2 шт"},{"name":"Капуста","amount":"200 г"},{"name":"Морковь","amount":"1 шт"},{"name":"Лук","amount":"1 шт"},{"name":"Томатная паста","amount":"1 ст. ложка"},{"name":"Масло","amount":"2 ст. ложки"},{"name":"Уксус","amount":"1 ч. ложка"},{"name":"Лавровый лист","amount":"1 шт"},{"name":"Перец","amount":"3 шт"},{"name":"Соль","amount":"по вкусу"},{"name":"Вода","amount":"1,5 л"}]""",
                steps = "Шаг 1: Нарежьте говядину крупными кусками и залейте холодной водой в кастрюле.\nШаг 2: Доведите до кипения, снимите пену, добавьте лавровый лист и перец, и варите бульон на слабом огне 1,5 часа.\nШаг 3: Пока варится бульон, натрите свёклу на крупной тёрке и обжарьте с уксусом и томатной пастой 5 минут.\nШаг 4: Нарежьте картофель кубиками и капусту тонкой соломкой.\nШаг 5: Добавьте в бульон картофель и капусту, варите 10 минут.\nШаг 6: Нарежьте морковь и лук, обжарьте их на масле, затем добавьте к бульону.\nШаг 7: Добавьте обжаренную свёклу, посолите и варите ещё 10 минут.\nШаг 8: Дайте борщу настояться под крышкой 15-20 минут перед подачей.",
                calories = 75, proteins = 7, fats = 3, carbs = 8,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe5}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 2, name = "Куриный суп с вермишелью", minutesToCook = 60,
                ingredients = """[{"name":"Куриное филе","amount":"200 г"},{"name":"Вермишель","amount":"50 г"},{"name":"Картофель","amount":"2 шт"},{"name":"Морковь","amount":"1 шт"},{"name":"Лук","amount":"1 шт"},{"name":"Соль, перец","amount":"по вкусу"}]""",
                steps = "Шаг 1: Положите куриное филе в кастрюлю, залейте водой и доведите до кипения.\nШаг 2: Снимите пену и варите на слабом огне 20 минут.\nШаг 3: Очистите картофель, нарежьте кубиками и добавьте в бульон.\nШаг 4: Нарежьте морковь и лук, добавьте в кастрюлю и варите ещё 10 минут.\nШаг 5: Всыпьте вермишель, посолите и поперчите по вкусу.\nШаг 6: Варите до готовности вермишели, около 5 минут.\nШаг 7: Подавайте с зеленью и сметаной.",
                calories = 60, proteins = 6, fats = 1, carbs = 8,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe6}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 2, name = "Томатный суп с базиликом", minutesToCook = 30,
                ingredients = """[{"name":"Помидоры","amount":"500 г"},{"name":"Лук","amount":"1 шт"},{"name":"Чеснок","amount":"2 зубчика"},{"name":"Овощной бульон","amount":"500 мл"},{"name":"Базилик","amount":"5-6 листьев"},{"name":"Оливковое масло","amount":"1 ст. ложка"},{"name":"Соль, перец","amount":"по вкусу"}]""",
                steps = "Шаг 1: Нарежьте помидоры, лук и чеснок.\nШаг 2: Разогрейте оливковое масло в кастрюле и обжарьте лук с чесноком.\nШаг 3: Добавьте нарезанные помидоры и тушите 10 минут.\nШаг 4: Влейте овощной бульон, добавьте базилик, соль и перец.\nШаг 5: Доведите до кипения и варите на слабом огне 15 минут.\nШаг 6: Измельчите суп блендером до однородной консистенции.\nШаг 7: Подавайте горячим, украсив листьями базилика.",
                calories = 45, proteins = 1, fats = 2, carbs = 7,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe7}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 2, name = "Грибной суп с картофелем", minutesToCook = 75,
                ingredients = """[{"name":"Грибы","amount":"300 г"},{"name":"Картофель","amount":"3 шт"},{"name":"Лук","amount":"1 шт"},{"name":"Морковь","amount":"1 шт"},{"name":"Сливки","amount":"100 мл"},{"name":"Соль, перец","amount":"по вкусу"}]""",
                steps = "Шаг 1: Очистите и нарежьте картофель кубиками.\nШаг 2: Нарежьте грибы и обжарьте их в масле до золотистой корочки.\nШаг 3: Нарежьте лук и морковь, добавьте к грибам и обжаривайте 5-7 минут.\nШаг 4: Залейте обжаренные грибы с овощами водой, добавьте картофель и доведите до кипения.\nШаг 5: Уменьшите огонь и варите суп 20 минут.\nШаг 6: Влейте сливки, посолите и поперчите по вкусу, перемешайте.\nШаг 7: Дайте супу немного настояться перед подачей.\nШаг 8: Подавайте с зеленью.",
                calories = 60, proteins = 2, fats = 3, carbs = 7,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe8}",
                isCustom = false
            ),

            // Вторые блюда
            DishEntity(
                id = 0, categoryId = 3, name = "Картофельное пюре с курицей", minutesToCook = 40,
                ingredients = """[{"name":"Картофель","amount":"500 г"},{"name":"Куриное филе","amount":"200 г"},{"name":"Сливочное масло","amount":"2 ст. ложки"},{"name":"Молоко","amount":"100 мл"},{"name":"Соль","amount":"по вкусу"}]""",
                steps = "Шаг 1: Очистите картофель, нарежьте и отварите в подсоленной воде до готовности.\nШаг 2: Нарежьте куриное филе кусочками и обжарьте на масле до золотистой корочки.\nШаг 3: Слейте воду с картофеля, добавьте сливочное масло и разомните.\nШаг 4: Влейте горячее молоко, перемешайте до однородной консистенции.\nШаг 5: Подавайте пюре с куриным филе.",
                calories = 140, proteins = 8, fats = 4, carbs = 18,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe9}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 3, name = "Запечённая свинина с овощами", minutesToCook = 90,
                ingredients = """[{"name":"Свинина","amount":"500 г"},{"name":"Морковь","amount":"2 шт"},{"name":"Картофель","amount":"3 шт"},{"name":"Лук","amount":"1 шт"},{"name":"Специи","amount":"по вкусу"}]""",
                steps = "Шаг 1: Нарежьте свинину кубиками и натрите специями.\nШаг 2: Нарежьте картофель и морковь крупными кусками, лук полукольцами.\nШаг 3: Выложите овощи и мясо в форму для запекания, перемешайте.\nШаг 4: Накройте форму фольгой и запекайте в разогретой духовке при 180 °C 1 час.\nШаг 5: Снимите фольгу и запекайте ещё 15-20 минут до золотистой корочки.\nШаг 6: Подавайте горячим с зеленью.",
                calories = 240, proteins = 15, fats = 12, carbs = 18,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe10}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 3, name = "Паста с курицей и сливками", minutesToCook = 30,
                ingredients = """[{"name":"Куриное филе","amount":"200 г"},{"name":"Паста","amount":"150 г"},{"name":"Сливки","amount":"100 мл"},{"name":"Чеснок","amount":"2 зубчика"},{"name":"Пармезан","amount":"20 г"}]""",
                steps = "Шаг 1: Отварите пасту в подсоленной воде до готовности.\nШаг 2: Нарежьте куриное филе небольшими кусочками.\nШаг 3: Обжарьте курицу с измельчённым чесноком на масле до золотистого цвета.\nШаг 4: Влейте сливки к курице, доведите до кипения и варите 5 минут.\nШаг 5: Добавьте к соусу отваренную пасту, перемешайте.\nШаг 6: Посыпьте тёртым пармезаном и оставьте на минуту под крышкой.\nШаг 7: Подавайте горячей, украсив зеленью.",
                calories = 260, proteins = 11, fats = 9, carbs = 32,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe11}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 3, name = "Жареная рыба с овощами", minutesToCook = 25,
                ingredients = """[{"name":"Филе рыбы","amount":"200 г"},{"name":"Морковь","amount":"1 шт"},{"name":"Лук","amount":"1 шт"},{"name":"Болгарский перец","amount":"1 шт"},{"name":"Специи","amount":"по вкусу"}]""",
                steps = "Шаг 1: Нарежьте филе рыбы порционными кусочками и посолите.\nШаг 2: Обжарьте рыбу на масле до золотистой корочки, затем отложите.\nШаг 3: Нарежьте лук, морковь и перец тонкими полосками.\nШаг 4: Обжарьте лук с морковью, добавьте перец и тушите 5 минут.\nШаг 5: Верните рыбу в сковороду и накройте крышкой.\nШаг 6: Готовьте ещё 5 минут, чтобы рыба пропиталась овощами.\nШаг 7: Подавайте с овощами и зеленью.",
                calories = 170, proteins = 18, fats = 7, carbs = 6,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe12}",
                isCustom = false
            ),

            // Салаты
            DishEntity(
                id = 0, categoryId = 4, name = "Цезарь с курицей", minutesToCook = 20,
                ingredients = """[{"name":"Куриное филе","amount":"150 г"},{"name":"Листья салата","amount":"50 г"},{"name":"Помидоры черри","amount":"6 шт"},{"name":"Сыр пармезан","amount":"20 г"},{"name":"Хлеб","amount":"2 ломтика"},{"name":"Оливковое масло","amount":"1 ст. ложка"},{"name":"Соль, перец","amount":"по вкусу"}]""",
                steps = "Шаг 1: Нарежьте куриное филе и обжарьте до золотистой корочки.\nШаг 2: Нарежьте хлеб кубиками и поджарьте до хруста.\nШаг 3: Выложите на тарелку листья салата, курицу и черри.\nШаг 4: Посыпьте салат пармезаном и сухариками.\nШаг 5: Полейте оливковым маслом и подавайте.",
                calories = 180, proteins = 12, fats = 9, carbs = 12,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe13}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 4, name = "Греческий салат", minutesToCook = 15,
                ingredients = """[{"name":"Огурец","amount":"1 шт"},{"name":"Помидор","amount":"2 шт"},{"name":"Перец сладкий","amount":"1 шт"},{"name":"Лук красный","amount":"1/2 шт"},{"name":"Фета","amount":"100 г"},{"name":"Оливки","amount":"10 шт"},{"name":"Оливковое масло","amount":"1 ст. ложка"},{"name":"Соль","amount":"по вкусу"}]""",
                steps = "Шаг 1: Нарежьте огурец, помидоры, перец и лук кубиками.\nШаг 2: Добавьте оливки и фету, нарезанную кубиками.\nШаг 3: Полейте салат оливковым маслом и перемешайте.\nШаг 4: Подавайте с зеленью.",
                calories = 130, proteins = 4, fats = 9, carbs = 8,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe14}",
                isCustom = false
            ),

            // Десерты
            DishEntity(
                id = 0, categoryId = 5, name = "Чизкейк классический", minutesToCook = 90,
                ingredients = """[{"name": "Печенье", "amount": "200 г"},{"name": "Сливочное масло", "amount": "100 г"},{"name": "Сливочный сыр", "amount": "500 г"},{"name": "Сахар", "amount": "150 г"},{"name": "Яйца", "amount": "3 шт"},{"name": "Ванильный экстракт", "amount": "1 ч. ложка"},{"name": "Сметана", "amount": "100 мл"}]""",
                steps = "Шаг 1: Размельчите печенье и смешайте с растопленным сливочным маслом.\nШаг 2: Выложите массу в форму, утрамбуйте и поставьте в холодильник на 15 минут.\nШаг 3: Взбейте сливочный сыр с сахаром до однородной консистенции.\nШаг 4: По одному добавляйте яйца, затем влейте ванильный экстракт и сметану.\nШаг 5: Вылейте смесь на основу и выпекайте при 160 °C 1 час.\nШаг 6: Остудите и уберите в холодильник на 3-4 часа перед подачей.",
                calories = 320, proteins = 6, fats = 22, carbs = 28,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe15}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 5, name = "Брауни с орехами", minutesToCook = 60,
                ingredients = """[{"name": "Шоколад", "amount": "200 г"},{"name": "Сливочное масло", "amount": "100 г"},{"name": "Сахар", "amount": "150 г"},{"name": "Яйца", "amount": "3 шт"},{"name": "Мука", "amount": "100 г"},{"name": "Орехи (грецкие или миндаль)", "amount": "100 г"}]""",
                steps = "Шаг 1: Растопите шоколад с маслом на водяной бане до однородной массы.\nШаг 2: Взбейте яйца с сахаром до лёгкой пены.\nШаг 3: Введите растопленный шоколад, перемешайте.\nШаг 4: Просейте муку и аккуратно вмешайте в тесто.\nШаг 5: Добавьте измельчённые орехи и перемешайте.\nШаг 6: Вылейте тесто в форму и выпекайте при 180 °C 25-30 минут.\nШаг 7: Остудите, нарежьте на порции и подавайте.",
                calories = 410, proteins = 7, fats = 24, carbs = 45,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe16}",
                isCustom = false
            ),
            DishEntity(
                id = 0, categoryId = 6, name = "Клубничный смузи", minutesToCook = 10,
                ingredients = """ [{"name": "Клубника", "amount": "200 г"},{"name": "Банан", "amount": "1 шт"},{"name": "Йогурт", "amount": "200 мл"},{"name": "Мёд", "amount": "1 ст. ложка"}]""",
                steps = "Шаг 1: Вымойте и очистите клубнику.\nШаг 2: Нарежьте банан на кусочки.\nШаг 3: Положите все ингредиенты в блендер.\nШаг 4: Взбейте до однородной консистенции.\nШаг 5: Подавайте охлаждённым, можно украсить свежей мятой.",
                calories = 80, proteins = 2, fats = 1, carbs = 16,
                imageUri = "android.resource://com.example.myhealthydiet/${R.drawable.recipe17}",
                isCustom = false
            )
        )
    }
}