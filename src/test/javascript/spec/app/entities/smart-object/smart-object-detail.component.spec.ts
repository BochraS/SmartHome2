/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { SmartObjectDetailComponent } from 'app/entities/smart-object/smart-object-detail.component';
import { SmartObject } from 'app/shared/model/smart-object.model';

describe('Component Tests', () => {
    describe('SmartObject Management Detail Component', () => {
        let comp: SmartObjectDetailComponent;
        let fixture: ComponentFixture<SmartObjectDetailComponent>;
        const route = ({ data: of({ smartObject: new SmartObject(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [SmartObjectDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmartObjectDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmartObjectDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smartObject).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
