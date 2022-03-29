package cc.woverflow.debugify.mixins.client.mc123739;

import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {

    /**
     * Method: reload
     * Anonymous Class: map.forEach((recipeBookGroup, list) -> {
     *     # HERE #
     * })
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "method_30279", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    private static Stream<RecipeResultCollection> test(Stream<List<Recipe<?>>> stream, Function<List<Recipe<?>>, RecipeResultCollection> function) {
        return stream
                .sorted((o1, o2) -> {
                    if (o1.isEmpty() || o2.isEmpty()) {
                        return 0;
                    }

                    return o1.get(0).getId().compareTo(o2.get(0).getId());
                }).map(function);
    }

}
