
export class Pattern {
  public static readonly vietnamesePattern = /^[0-9a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\s\-\_,.]+$/;
  public static readonly endPointPattern = /^\/api\/[a-zA-Z]+(?:-[a-zA-Z]+)*\/[a-zA-Z]+(?:-[a-zA-Z]+)*$/;
  public static readonly phonePattern = /^(0|\+?\d{1,3})\d{9,10}$/;
  public static readonly websitePattern = /^((https|http)?:\/\/)?(www\.)?[a-zA-Z0-9-]+(\.[a-zA-Z]{2,})+(:\d+)?(\/.*)?$/;
}
