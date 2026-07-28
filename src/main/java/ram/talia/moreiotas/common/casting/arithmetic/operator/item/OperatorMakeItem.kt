package ram.talia.moreiotas.common.casting.arithmetic.operator.item

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.any
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import net.minecraft.world.item.ItemStack
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextPositiveInt
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_STACK
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_TYPE

object OperatorMakeItem : OperatorBasic(2, IotaMultiPredicate.pair(any(ofType(ITEM_TYPE), ofType(ITEM_STACK)), ofType(DOUBLE.get())))  {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val (idx, typeOrStack) = it.next()
        val count = it.nextPositiveInt(arity)

        return when (typeOrStack) {
            is ItemTypeIota -> {
                ItemStack(typeOrStack.item, count).asActionResult
            }
            is ItemStackIota -> {
                typeOrStack.itemStack.copyWithCount(count).asActionResult
            }
            else -> throw IllegalStateException("iota argument to OperatorMakeItem must be one of ItemType or ItemStack.")
        }
    }
}