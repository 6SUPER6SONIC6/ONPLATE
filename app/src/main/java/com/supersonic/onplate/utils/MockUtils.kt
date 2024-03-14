package com.supersonic.onplate.utils

import com.supersonic.onplate.models.Recipe
import com.supersonic.onplate.pages.newRecipe.directions.Step
import com.supersonic.onplate.pages.newRecipe.ingredients.Ingredient

val mockListIngredients = listOf(
    "8 good-quality pork sausages",
    "1 kg beef mince",
    "1 onion, finely chopped",
    "½ a large bunch flat-leaf parsley, finely chopped",
    "85g parmesan, grated, plus extra to serve (optional)",
    "100g fresh breadcrumbs",
    "2 eggs, beaten with a fork",
    "olive oil, for roasting",
    "spaghetti, to serve (about 100g per portion)")

val mockListDirections = listOf("First, make the meatballs." +
        "Split the skins of the sausages and squeeze out the" +
        " meat into a large mixing bowl.",
    "Add the mince, onion, parsley, parmesan, breadcrumbs, eggs and lots of seasoning." +
            " Get your hands in and mix together really well – the more you squeeze" +
            " and mash the mince, the more tender the meatballs will be.",
    "Heat the oven to 220C/200C fan/gas 7. Roll the mince mixture into about" +
            " 50 golf-ball-sized meatballs. Set aside any meatballs for freezing," +
            " allowing about five per portion, then spread the rest out in a large" +
            " roasting tin – the meatballs will brown better if spaced out a bit.",
    "Drizzle with a little oil (about 1 tsp per portion), shake to coat, then roast for 20-30 mins until browned.",
    "Meanwhile, make the sauce. Heat the olive oil in a large saucepan. Add the garlic cloves and sizzle for 1 min.",
    "Stir in the chopped tomatoes, red wine, if using, caster sugar, parsley and seasoning. Simmer for 15-20 mins " +
            "until slightly thickened.",
    "Stir in a few basil leaves, if using, spoon out any portions for freezing, then add the cooked meatballs to the " +
            "pan to keep warm while you cook the spaghetti in a pan of boiling, salted water.",
    "Spoon the sauce and meatballs over spaghetti, or stir them all together and serve with extra " +
            "parmesan and a few basil leaves, if you like.")


private fun loadMockIngredients() : List<Ingredient> {

    val ingredients : MutableList<Ingredient> = emptyList<Ingredient>().toMutableList()

    mockListIngredients.forEachIndexed { index, item ->
        ingredients.add(index, Ingredient(index, item))
    }

    return ingredients
}
private fun loadMockDirections() : List<Step> {

    val directions : MutableList<Step> = emptyList<Step>().toMutableList()

    mockListDirections.forEachIndexed { index, item ->
        directions.add(index, Step(index, item))
    }

    return directions
}

object MockUtils {
    fun loadMockRecipes(): List<Recipe> {
        return listOf(
//            Recipe(
//                0,
//                "Pasta with meatballs",
//                "Spaghetti and meatballs are a classic family-friendly dinner. This recipe is great for batch cooking so you can save extra portions in the freezer.",
//                loadMockIngredients(),
//                loadMockDirections(),
//                2,
//                20,
//                "2 hours 20 minutes"
////                listOf(
////                    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
////                    "https://s23209.pcdn.co/wp-content/uploads/2022/09/220223_DD_Spaghetti-Meatballs_198.jpg",
////                    "https://realfood.tesco.com/media/images/RFO-Meatballs-LargeHero-1400x919-070796c4-0009-4b24-ac5a-b00e1085ecea-0-1400x920.jpg",
////                    "https://cravinghomecooked.com/wp-content/uploads/2020/02/spaghetti-and-meatballs-1.jpg",
////                    "https://www.simplyrecipes.com/thmb/Boo37yZBqeSpmELBIP_BBX_yVlU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Spaghetti-And-Meatballs-LEAD-3-40bdae68ea144751a8e0a4b0f972af2d.jpg",
////                    "https://www.courtneyssweets.com/wp-content/uploads/2018/09/garlic-olive-oil-pasta-with-meatballs-and-spinach-8.jpg",
////                )
//            ),
/*
            Recipe(
                1,
                "Chilli con carne",
                "This great chilli recipe has to be one of the best dishes to serve to friends for a casual get-together. An easy sharing favourite that uses up storecupboard ingredients.",
                listOf("1 large onion",
                    "1 red pepper",
                    "2 garlic cloves",
                    "1 tbsp oil",
                    "1 tsp paprika",
                    "1 tsp ground cumin",
                    "500g lean minced beef",
                    "1 beef stock cube",
                    "400g can chopped tomatoes",
                    "½ tsp dried marjoram",
                    "1 tsp sugar (or add a thumbnail-sized piece of dark chocolate along with the beans instead, see tip)\n",
                    "2 tbsp tomato purée",
                    "410g can red kidney beans",
                    "plain boiled long grain rice, to serve",
                    "soured cream, to serve",
                    ),
                listOf("Prepare your vegetables. Chop 1 large onion into small dice, about 5mm square. The easiest way to do this is to cut the onion in half from root to tip, peel it and slice each half into thick matchsticks lengthways, not quite cutting all the way to the root end so they are still held together. Slice across the matchsticks into neat dice.",
                    "Cut 1 red pepper in half lengthways, remove stalk and wash the seeds away, then chop. Peel and finely chop 2 garlic cloves.",
                    "Start cooking. Put your pan on the hob over a medium heat. Add 1 tbsp oil and leave it for 1-2 minutes until hot (a little longer for an electric hob).",
                    "Add the onion and cook, stirring fairly frequently, for about 5 minutes, or until the onion is soft, squidgy and slightly translucent.",
                    "Tip in the garlic, red pepper, 1 heaped tsp hot chilli powder or 1 level tbsp mild chilli powder, 1 tsp paprika and 1 tsp ground cumin.",
                    "Give it a good stir, then leave it to cook for another 5 minutes, stirring occasionally.",
                    "Brown 500g lean minced beef. Turn the heat up a bit, add the meat to the pan and break it up with your spoon or spatula. The mix should sizzle a bit when you add the mince.",
                    "Keep stirring and prodding for at least 5 minutes, until all the mince is in uniform, mince-sized lumps and there are no more pink bits. Make sure you keep the heat hot enough for the meat to fry and become brown, rather than just stew.",
                    "Make the sauce. Crumble 1 beef stock cube into 300ml hot water. Pour this into the pan with the mince mixture.",
                    "Add a 400g can of chopped tomatoes. Tip in ½ tsp dried marjoram, 1 tsp sugar and add a good shake of salt and pepper. Squirt in about 2 tbsp tomato purée and stir the sauce well.",
                    "Simmer it gently. Bring the whole thing to the boil, give it a good stir and put a lid on the pan. Turn down the heat until it is gently bubbling and leave it for 20 minutes.",
                    "Check on the pan occasionally to stir it and make sure the sauce doesn’t catch on the bottom of the pan or isn’t drying out. If it is, add a couple of tablespoons of water and make sure that the heat really is low enough. After simmering gently, the saucy mince mixture should look thick, moist and juicy.",
                    "Drain and rinse a 410g can of red kidney beans in a sieve and stir them into the chilli pot. Bring to the boil again, and gently bubble without the lid for another 10 minutes, adding a little more water if it looks too dry.",
                    "Taste a bit of the chilli and season. It will probably take a lot more seasoning than you think.",
                    "Now replace the lid, turn off the heat and leave your chilli to stand for 10 minutes before serving. This is really important as it allows the flavours to mingle",
                    "Serve with soured cream and plain boiled long grain rice."
                    ),
                60,
                listOf(
                    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1001451_6-c8d72b8.jpg?resize=768,574",
                    "https://www.allrecipes.com/thmb/jkfRQlj_grQQU3KvD-2Z3k3Sa_Q=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/89993-award-winning-chili-con-carne-ddmfs-3x4-4e3a48f5505a4c998415c261507b0a76.jpg",
                    "https://mccormick.widen.net/content/114k0awocf/original/hot_chilli_con_carne_637390546052910843_800x800.jpg",
                    "https://www.errenskitchen.com/wp-content/uploads/2019/02/Chili-con-carne-1200.jpg",
                )
            ),

            Recipe(
                2,
                "Pasta with meatballs",
                "Spaghetti and meatballs are a classic family-friendly dinner. This recipe is great for batch cooking so you can save extra portions in the freezer.",
                listOf("8 good-quality pork sausages",
                    "1 kg beef mince",
                    "1 onion, finely chopped",
                    "½ a large bunch flat-leaf parsley, finely chopped",
                    "85g parmesan, grated, plus extra to serve (optional)",
                    "100g fresh breadcrumbs",
                    "2 eggs, beaten with a fork",
                    "olive oil, for roasting",
                    "spaghetti, to serve (about 100g per portion)"),
                listOf("First, make the meatballs." +
                        "Split the skins of the sausages and squeeze out the" +
                        " meat into a large mixing bowl.",
                    "Add the mince, onion, parsley, parmesan, breadcrumbs, eggs and lots of seasoning." +
                            " Get your hands in and mix together really well – the more you squeeze" +
                            " and mash the mince, the more tender the meatballs will be.",
                    "Heat the oven to 220C/200C fan/gas 7. Roll the mince mixture into about" +
                            " 50 golf-ball-sized meatballs. Set aside any meatballs for freezing," +
                            " allowing about five per portion, then spread the rest out in a large" +
                            " roasting tin – the meatballs will brown better if spaced out a bit.",
                    "Drizzle with a little oil (about 1 tsp per portion), shake to coat, then roast for 20-30 mins until browned.",
                    "Meanwhile, make the sauce. Heat the olive oil in a large saucepan. Add the garlic cloves and sizzle for 1 min.",
                    "Stir in the chopped tomatoes, red wine, if using, caster sugar, parsley and seasoning. Simmer for 15-20 mins " +
                            "until slightly thickened.",
                    "Stir in a few basil leaves, if using, spoon out any portions for freezing, then add the cooked meatballs to the " +
                            "pan to keep warm while you cook the spaghetti in a pan of boiling, salted water.",
                    "Spoon the sauce and meatballs over spaghetti, or stir them all together and serve with extra " +
                            "parmesan and a few basil leaves, if you like."),
                45,
                listOf(
                    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1035708_10-fdc5ae0.jpg?quality=90&webp=true&resize=440,400",
                    "https://s23209.pcdn.co/wp-content/uploads/2022/09/220223_DD_Spaghetti-Meatballs_198.jpg",
                    "https://realfood.tesco.com/media/images/RFO-Meatballs-LargeHero-1400x919-070796c4-0009-4b24-ac5a-b00e1085ecea-0-1400x920.jpg",
                    "https://cravinghomecooked.com/wp-content/uploads/2020/02/spaghetti-and-meatballs-1.jpg",
                    "https://www.simplyrecipes.com/thmb/Boo37yZBqeSpmELBIP_BBX_yVlU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Spaghetti-And-Meatballs-LEAD-3-40bdae68ea144751a8e0a4b0f972af2d.jpg",
                    "https://www.courtneyssweets.com/wp-content/uploads/2018/09/garlic-olive-oil-pasta-with-meatballs-and-spinach-8.jpg",
                )
            ),

            Recipe(
                1,
                "Chilli con carne",
                "This great chilli recipe has to be one of the best dishes to serve to friends for a casual get-together. An easy sharing favourite that uses up storecupboard ingredients.",
                listOf("1 large onion",
                    "1 red pepper",
                    "2 garlic cloves",
                    "1 tbsp oil",
                    "1 tsp paprika",
                    "1 tsp ground cumin",
                    "500g lean minced beef",
                    "1 beef stock cube",
                    "400g can chopped tomatoes",
                    "½ tsp dried marjoram",
                    "1 tsp sugar (or add a thumbnail-sized piece of dark chocolate along with the beans instead, see tip)\n",
                    "2 tbsp tomato purée",
                    "410g can red kidney beans",
                    "plain boiled long grain rice, to serve",
                    "soured cream, to serve",
                ),
                listOf("Prepare your vegetables. Chop 1 large onion into small dice, about 5mm square. The easiest way to do this is to cut the onion in half from root to tip, peel it and slice each half into thick matchsticks lengthways, not quite cutting all the way to the root end so they are still held together. Slice across the matchsticks into neat dice.",
                    "Cut 1 red pepper in half lengthways, remove stalk and wash the seeds away, then chop. Peel and finely chop 2 garlic cloves.",
                    "Start cooking. Put your pan on the hob over a medium heat. Add 1 tbsp oil and leave it for 1-2 minutes until hot (a little longer for an electric hob).",
                    "Add the onion and cook, stirring fairly frequently, for about 5 minutes, or until the onion is soft, squidgy and slightly translucent.",
                    "Tip in the garlic, red pepper, 1 heaped tsp hot chilli powder or 1 level tbsp mild chilli powder, 1 tsp paprika and 1 tsp ground cumin.",
                    "Give it a good stir, then leave it to cook for another 5 minutes, stirring occasionally.",
                    "Brown 500g lean minced beef. Turn the heat up a bit, add the meat to the pan and break it up with your spoon or spatula. The mix should sizzle a bit when you add the mince.",
                    "Keep stirring and prodding for at least 5 minutes, until all the mince is in uniform, mince-sized lumps and there are no more pink bits. Make sure you keep the heat hot enough for the meat to fry and become brown, rather than just stew.",
                    "Make the sauce. Crumble 1 beef stock cube into 300ml hot water. Pour this into the pan with the mince mixture.",
                    "Add a 400g can of chopped tomatoes. Tip in ½ tsp dried marjoram, 1 tsp sugar and add a good shake of salt and pepper. Squirt in about 2 tbsp tomato purée and stir the sauce well.",
                    "Simmer it gently. Bring the whole thing to the boil, give it a good stir and put a lid on the pan. Turn down the heat until it is gently bubbling and leave it for 20 minutes.",
                    "Check on the pan occasionally to stir it and make sure the sauce doesn’t catch on the bottom of the pan or isn’t drying out. If it is, add a couple of tablespoons of water and make sure that the heat really is low enough. After simmering gently, the saucy mince mixture should look thick, moist and juicy.",
                    "Drain and rinse a 410g can of red kidney beans in a sieve and stir them into the chilli pot. Bring to the boil again, and gently bubble without the lid for another 10 minutes, adding a little more water if it looks too dry.",
                    "Taste a bit of the chilli and season. It will probably take a lot more seasoning than you think.",
                    "Now replace the lid, turn off the heat and leave your chilli to stand for 10 minutes before serving. This is really important as it allows the flavours to mingle",
                    "Serve with soured cream and plain boiled long grain rice."
                ),
                60,
                listOf(
                    "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/recipe-image-legacy-id-1001451_6-c8d72b8.jpg?resize=768,574",
                    "https://www.allrecipes.com/thmb/jkfRQlj_grQQU3KvD-2Z3k3Sa_Q=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/89993-award-winning-chili-con-carne-ddmfs-3x4-4e3a48f5505a4c998415c261507b0a76.jpg",
                    "https://mccormick.widen.net/content/114k0awocf/original/hot_chilli_con_carne_637390546052910843_800x800.jpg",
                    "https://www.errenskitchen.com/wp-content/uploads/2019/02/Chili-con-carne-1200.jpg",
                )
            ),

 */
        )
    }
}