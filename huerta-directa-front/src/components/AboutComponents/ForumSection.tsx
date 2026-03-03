import { Button } from "../GlobalComponents/Button";
import { faArrowRight, faCommentDots } from "@fortawesome/free-solid-svg-icons";

interface Comment {
  id: number;
  commentType: "SITE" | "PRODUCT";
  commentCommenter: string;
  creationComment: string;
  user: {
    name: string;
    email: string;
  };
}

interface ForumSectionProps {
  comments: Comment[];
  userRole: string;
}

export const ForumSection = ({ comments, userRole }: ForumSectionProps) => {
  const dashboardRoute =
    userRole === "Administrador" ? "/DashboardAdmin" : "/MensajesComentarios";

  return (
    <section className="bg-white max-w-350 mx-auto my-20 p-8 rounded-xl shadow-lg">
      <div className="space-y-6">
        <h2 className="text-3xl font-bold text-gray-900 border-b border-gray-300 pb-4">
          Foro De Huerta Directa
        </h2>

        {/* GRID */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {comments.map((comment) => (
            <div
              key={comment.id}
              className="bg-[#eaf5e7] rounded-xl shadow-lg p-6 flex flex-col justify-between hover:shadow-2xl transition-all duration-500 hover:scale-[1.03] ease-in-out cursor-pointer group"
            >
              <div className="flex items-start space-x-4 mb-4">
                {/* Avatar */}
                <div className="shrink-0 bg-[#8bc34a] w-12 h-12 rounded-full flex items-center justify-center">
                  <span className="text-white text-xl font-bold uppercase">
                    {comment.user.name.charAt(0)}
                  </span>
                </div>

                {/* Content */}
                <div className="flex-1">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="text-lg font-semibold text-gray-900 leading-tight">
                        {comment.commentType === "SITE"
                          ? "Comentario del sitio"
                          : "Comentario del producto"}
                      </p>

                      <p className="text-sm text-gray-500 mt-1">
                        {comment.user.name}
                        <span className="hidden sm:inline">
                          {" "}
                          • {comment.user.email}
                        </span>
                      </p>
                    </div>
                  </div>

                  <p className="text-xs text-gray-500 mt-2">
                    {new Date(comment.creationComment).toLocaleDateString(
                      "es-CO",
                      {
                        day: "2-digit",
                        month: "long",
                        year: "numeric",
                      },
                    )}
                  </p>

                  <p className="mt-3 text-gray-700 line-clamp-3 break-all whitespace-normal max-w-75">
                    {comment.commentCommenter}
                  </p>
                </div>
              </div>

              {/* Responder */}
              <div className="flex justify-end mt-4">
                <Button
                  text="Responder"
                  iconLetf={faCommentDots}
                  iconRight={faArrowRight}
                  className="px-4 py-2 text-sm"
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* NOTA */}
      <div className="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-800 p-4 rounded-md shadow-md w-full mt-16 flex justify-between items-center hover:bg-yellow-100/80 transition-all duration-500">
        <p className="text-md">
          <strong>Nota:</strong> Para eliminar o actualizar los comentarios
          debes ir al <span className="font-semibold">dashboard</span> en la
          sección de mensajes.
        </p>

        <Button
          to={dashboardRoute}
          text="Ir a dashboard"
          iconRight={faArrowRight}
          className="py-3 px-8"
        />
      </div>
    </section>
  );
};
