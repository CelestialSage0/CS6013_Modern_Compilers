import fitz  # PyMuPDF
import sys

def split_pdf(input_pdf, output_pdf):
    doc = fitz.open(input_pdf)
    new_doc = fitz.open()

    for page in doc:
        rect = page.rect
        w = rect.width
        h = rect.height

        # Define 4 quadrants
        clips = [
            fitz.Rect(0, 0, w/2, h/2),      # top-left
            fitz.Rect(w/2, 0, w, h/2),      # top-right
            fitz.Rect(0, h/2, w/2, h),      # bottom-left
            fitz.Rect(w/2, h/2, w, h)       # bottom-right
        ]

        for clip in clips:
            new_page = new_doc.new_page(width=clip.width, height=clip.height)
            new_page.show_pdf_page(new_page.rect, doc, page.number, clip=clip)

    new_doc.save(output_pdf)
    print(f"Saved: {output_pdf}")


if __name__ == "__main__":
    input_pdf = sys.argv[1]
    output_pdf = sys.argv[2]
    split_pdf(input_pdf, output_pdf)
