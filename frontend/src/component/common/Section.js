import "./css/Section.css"

export default function Section({children}) {
    return (
        <section className={"content-wrapper"}>
            <div className={"content"}>
                {children}
            </div>
        </section>
    );
}