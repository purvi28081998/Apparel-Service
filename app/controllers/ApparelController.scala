package controllers

import javax.inject._
import play.api.mvc._
import services.ApparelService
import models.Apparel
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

@Singleton
class ApparelController @Inject()(cc: ControllerComponents, apparelService: ApparelService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def listApparels: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    apparelService.allApparels().map { apparels =>
      Ok(Json.toJson(apparels))
    }
  }

  def addApparel: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val apparelData = request.body.asFormUrlEncoded.get
    val name = apparelData("name").head
    val price = apparelData("price").head.toInt
    val stock = apparelData("stock").head.toInt
    val apparel = Apparel(None, name, price, stock)
    apparelService.addApparel(apparel).map(_ =>
      Created(Json.toJson(apparel))
    )
  }

  def deleteApparel(id: Long): Action[AnyContent] = Action.async {
    apparelService.deleteApparel(id).map { result =>
      if (result) {
        Ok(s"Delete successful")
      } else {
         NotFound("Error occured.")
      }
    }
  }


  def updateCart(userID: Long, apparelID: Long, quantity: Int, price: Int, flag: Int): Action[AnyContent] = Action.async {
    apparelService.updateCart(userID, apparelID, quantity, price, flag).map { result =>
      Ok("Update successful")
    } recover {
      case ex: Exception =>
        InternalServerError("An error occurred: " + ex.getMessage)
    }
  }




}