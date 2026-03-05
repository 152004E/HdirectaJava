import { AboutSection } from "../../components/AboutComponents/AboutSection"
import { ForumSection } from "../../components/AboutComponents/ForumSection"

const QuienesSomos = () => {
  return (
    <div className="">
      <AboutSection/>
      <ForumSection comments={[]} userRole={""} />
    </div>
  )
}

export default QuienesSomos
